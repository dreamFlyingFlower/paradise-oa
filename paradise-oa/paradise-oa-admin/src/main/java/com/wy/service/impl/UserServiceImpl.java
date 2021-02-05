package com.wy.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.wy.base.AbstractService;
import com.wy.common.Constants;
import com.wy.component.AsyncService;
import com.wy.component.MessageService;
import com.wy.component.RedisService;
import com.wy.component.TokenService;
import com.wy.crypto.CryptoUtils;
import com.wy.enums.CommonEnum;
import com.wy.enums.TipEnum;
import com.wy.enums.UserState;
import com.wy.exception.AuthException;
import com.wy.mapper.DepartMapper;
import com.wy.mapper.RoleMapper;
import com.wy.mapper.UserMapper;
import com.wy.mapper.UserRoleMapper;
import com.wy.mapper.UserinfoMapper;
import com.wy.model.Fileinfo;
import com.wy.model.Menu;
import com.wy.model.Role;
import com.wy.model.User;
import com.wy.model.UserExample;
import com.wy.model.UserRole;
import com.wy.model.UserRoleExample;
import com.wy.properties.ConfigProperties;
import com.wy.result.ResultException;
import com.wy.service.FileinfoService;
import com.wy.service.UserService;
import com.wy.util.FilesUtils;
import com.wy.util.SecurityUtils;
import com.wy.util.ServletUtils;
import com.wy.utils.ListUtils;
import com.wy.utils.StrUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Service("userService")
@Slf4j
public class UserServiceImpl extends AbstractService<User, Long> implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private DepartMapper departMapper;

	@Autowired
	private UserinfoMapper userinfoMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private ConfigProperties config;

	@Autowired
	private AsyncService asyncService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private FileinfoService fileinfoService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private MenuServiceImpl menuServiceImpl;

	/**
	 * 校验用户是否允许操作
	 * 
	 * @param user 用户信息
	 */
	public void assertModifyed(User user) {
		if (ListUtils.isNotBlank(user.getRoles())) {
			if (user.getRoles().get(0).getRoleType() == 0) {
				throw new ResultException("不允许操作超级管理员用户");
			}
		} else {
			List<Role> roles = roleMapper.selectByUserId(user.getUserId());
			if (ListUtils.isBlank(roles)) {
				throw new ResultException(TipEnum.TIP_USER_NOT_DISTRIBUTE_ROLE);
			} else {
				if (user.getRoles().get(0).getRoleType() == 0) {
					throw new ResultException("不允许操作超级管理员用户");
				}
			}
		}
	}

	/**
	 * 判断原始密码是否符合条件
	 * 
	 * @param password 前端传到后台的密码:AES加密(原始密码_时间戳)
	 */
	@Override
	public String assertPassword(String password) {
		password = CryptoUtils.AESSimpleCrypt(config.getLogin().getSecretKeyUser(), password, false);
		password = password.substring(0, password.lastIndexOf("_"));
		return assertOriginalPassword(password);
	}

	/**
	 * 检查原始密码是否符合条件
	 * 
	 * @param password 原始密码
	 * @return 原始密码
	 */
	private String assertOriginalPassword(String password) {
		if (password.length() < Constants.PWD_MIN_LENGTH || password.length() > Constants.PWD_MAX_LENGTH) {
			throw new AuthException(
					messageService.getMessage("msg_pwd_length", Constants.PWD_MIN_LENGTH, Constants.PWD_MAX_LENGTH));
		}
		return password;
	}

	/**
	 * 通过用户名,邮件,手机号查询用户信息
	 * 
	 * @param username 用户名或邮件或手机号
	 * @return 用户信息
	 */
	@Override
	public User getByUsername(String username) {
		List<User> entitys = userMapper.selectByUsername(username);
		if (ListUtils.isBlank(entitys) || entitys.size() > 1) {
			return null;
		}
		return entitys.get(0);
	}

	@Override
	public int update(User t) {
		assertModifyed(t);
		// 修改缓存信息
		tokenService.refreshToken(t);
		return super.update(t);
	}

	/**
	 * spring security登录校验,除了用户名和密码之外,其他参数从request工具中拿
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		handlerCaptcha(username);
		User user = getByUsername(username);
		if (Objects.isNull(user)) {
			return null;
		}
		handlerState(user);
		handlerPermission(user);
		return user;
	}

	/**
	 * 验证码处理
	 * 
	 * @param username 登录用户名
	 */
	private void handlerCaptcha(String username) {
		if (!config.getCaptcha().isValid()) {
			return;
		}
		String captcha = ServletUtils.getParameter("captcha");
		String uuidKey = ServletUtils.getParameter("uuid");
		if (StrUtils.isBlank(captcha) || StrUtils.isBlank(uuidKey)) {
			throw new AuthException(TipEnum.TIP_LOGIN_CAPTCHA_EMPTY);
		}
		String redisCaptcha = redisService.getStr(uuidKey);
		if (StrUtils.isBlank(redisCaptcha)) {
			asyncService.recordLoginLog(username, CommonEnum.NO.ordinal(),
					TipEnum.TIP_LOGIN_CAPTCHA_NOT_EXIST.getMsg());
			throw new ResultException(TipEnum.TIP_LOGIN_CAPTCHA_NOT_EXIST);
		}
		if (!Objects.equals(captcha, redisCaptcha)) {
			asyncService.recordLoginLog(username, CommonEnum.NO.ordinal(), TipEnum.TIP_LOGIN_CAPTCHA_ERROR.getMsg());
			throw new ResultException(TipEnum.TIP_LOGIN_CAPTCHA_ERROR);
		}
		// 验证过后删除session中的缓存
		redisService.delete(uuidKey);
	}

	/**
	 * 用户状态处理
	 * 
	 * @param user 用户信息
	 */
	private void handlerState(User user) {
		if (UserState.USER_BLACK.getCode() == user.getState()) {
			log.info("登录用户:{}是黑名单用户", user.getUsername());
			throw new AuthException(TipEnum.TIP_USER_IS_BLACK);
		}
		if (UserState.USER_DELETE.getCode() == user.getState()) {
			log.info("登录用户:{}已被删除", user.getUsername());
			throw new AuthException(TipEnum.TIP_USER_NOT_AVALIABLE);
		}
		if (UserState.USER_LOCK.getCode() == user.getState()) {
			log.info("登录用户:{}为锁定状态", user.getUsername());
			throw new AuthException(messageService.getMessage("msg_user_lock", config.getLogin().getInterval()));
		}
	}

	/**
	 * 处理用户角色权限信息
	 * 
	 * @param user 用户信息
	 */
	private void handlerPermission(User user) {
		List<Menu> permissionVos = menuServiceImpl.getTreeByUserId(user.getUserId());
		user.setMenus(permissionVos);
	}

	/**
	 * 新增保存用户信息
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	@Override
	@Transactional
	public Object insertSelective(User user) {
		// 密码的形式为:AES加密(密码_当前时间戳)
		String password = user.getPassword();
		if (StrUtils.isBlank(password)) {
			password = config.getCommon().getDefaultPwd();
		} else {
			String temp_pwd = CryptoUtils.AESSimpleCrypt(config.getLogin().getSecretKeyUser(), password, false);
			password = temp_pwd.substring(0, temp_pwd.lastIndexOf("_"));
			if (password.length() > 16) {
				throw new ResultException("密码长度不能超过16位");
			}
		}
		user.setPassword(SecurityUtils.encode(password));
		// 新增用户信息
		int rows = userMapper.insertSelective(user);
		// 新增用户与角色管理
		insertUserRole(user);
		return rows;
	}

	/**
	 * 修改保存用户信息
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	@Override
	@Transactional
	public int updateSelective(User user) {
		assertModifyed(user);
		// 删除用户与角色关联
		UserRoleExample example = new UserRoleExample();
		example.createCriteria().andUserIdEqualTo(user.getUserId());
		userRoleMapper.deleteByExample(example);
		// 新增用户与角色管理
		insertUserRole(user);
		return userMapper.updateByPrimaryKeySelective(user);
	}

	/**
	 * 修改用户头像
	 * 
	 * @param userId 用户ID
	 * @param avatar 头像地址
	 * @return 结果
	 */
	@Override
	public User updateAvatar(MultipartFile file) {
		User loginUser = SecurityUtils.getLoginUser();
		Fileinfo fileinfo = fileinfoService.upload(file);
		userMapper.updateByPrimaryKeySelective(
				User.builder().userId(loginUser.getUserId()).avatar(fileinfo.getFileUrl()).build());
		loginUser.setAvatar(fileinfo.getFileUrl());
		tokenService.refreshToken(loginUser);
		// 原头像地址
		String originalAvatar = loginUser.getAvatar();
		// 删除本地原头像
		if (StrUtils.isNotBlank(originalAvatar)) {
			File localFile = FilesUtils.getLocalPathByHttp(fileinfo.getFileUrl());
			if (localFile.exists() && localFile.isFile()) {
				localFile.delete();
			}
		}
		return loginUser;
	}

	/**
	 * 重置用户密码
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	@Override
	public int resetPwd(User user) {
		assertModifyed(user);
		user.setPassword(SecurityUtils.encode(user.getPassword()));
		return userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public int resetUserPwd(Long userId, String oldPassword, String newPassword) {
		User user = tokenService.getLoginUser(userId);
		String password = user.getPassword();
		oldPassword = CryptoUtils.AESSimpleCrypt(config.getLogin().getSecretKeyUser(), oldPassword, false);
		oldPassword = oldPassword.substring(0, oldPassword.lastIndexOf("_"));
		if (!SecurityUtils.matches(oldPassword, password)) {
			throw new ResultException(TipEnum.TIP_USER_PWD_ORIGINAL_ERROR);
		}
		newPassword = CryptoUtils.AESSimpleCrypt(config.getLogin().getSecretKeyUser(), newPassword, false);
		newPassword = newPassword.substring(0, newPassword.lastIndexOf("_"));
		if (SecurityUtils.matches(newPassword, password)) {
			throw new ResultException(TipEnum.TIP_USER_PWD_OLD_NOT_SAME_NEW);
		}
		int row = userMapper.updateByPrimaryKeySelective(
				User.builder().userId(userId).password(SecurityUtils.encode(newPassword)).build());
		if (row > 0) {
			// 更新缓存用户密码
			user.setPassword(SecurityUtils.encode(newPassword));
			tokenService.refreshToken(user);
		}
		return row;
	}

	@Override
	public Object getById(Long userId) {
		User user = userMapper.selectByPrimaryKey(userId);
		user.setUserinfo(userinfoMapper.selectByPrimaryKey(userId));
		if (!Objects.isNull(user.getDepartId())) {
			user.setDepart(departMapper.selectByPrimaryKey(user.getDepartId()));
		}
		List<Role> roles = roleMapper.selectByUserId(userId);
		user.setRoles(roles);
		return user;
	}

	/**
	 * 新增用户角色信息
	 * 
	 * @param user 用户对象
	 */
	public void insertUserRole(User user) {
		List<Role> roles = user.getRoles();
		if (ListUtils.isBlank(roles)) {
			return;
		}
		List<UserRole> list = new ArrayList<UserRole>();
		for (Role role : roles) {
			list.add(UserRole.builder().userId(user.getUserId()).roleId(role.getRoleId()).build());
		}
		userRoleMapper.inserts(list);
	}

	/**
	 * 通过用户ID删除用户
	 * 
	 * @param userId 用户ID
	 * @return 结果
	 */
	@Override
	public int delete(Long userId) {
		assertModifyed(User.builder().userId(userId).build());
		// 删除用户与角色关联
		UserRoleExample example = new UserRoleExample();
		example.createCriteria().andUserIdEqualTo(userId);
		userRoleMapper.deleteByExample(example);
		return userMapper.deleteByPrimaryKey(userId);
	}

	/**
	 * 批量删除用户信息
	 * 
	 * @param userIds 需要删除的用户ID
	 * @return 结果
	 */
	@Override
	public int deletes(List<Long> userIds) {
		List<User> users = userMapper.selectUserRoles(userIds);
		users.forEach(t -> {
			if (ListUtils.isNotBlank(t.getRoles()) && t.getRoles().get(0).getRoleType() == 0) {
				throw new ResultException(TipEnum.TIP_ROLE_NOT_OPERATE_ADMIN);
			}
		});
		return userMapper.deleteByPrimaryKeys(userIds);
	}

	/**
	 * 导入用户数据
	 * 
	 * @param userList 用户数据列表
	 * @param operName 操作用户
	 * @return 结果
	 */
	@Override
	public int importUser(List<User> users, String operName) {
		if (ListUtils.isBlank(users)) {
			throw new ResultException("导入用户数据不能为空!");
		}
		assertUnique(users);
		return 1;
	}

	/**
	 * 批量导入检查用户名,邮箱,手机号唯一性
	 * 
	 * @param users 用户信息
	 */
	private void assertUnique(List<User> users) {
		List<String> usernames = new ArrayList<String>();
		List<String> emails = new ArrayList<String>();
		List<String> mobiles = new ArrayList<String>();
		users.stream().forEach(t -> {
			usernames.add(t.getUsername());
			emails.add(t.getEmail());
			mobiles.add(t.getMobile());
			if (StrUtils.isBlank(t.getPassword())) {
				t.setPassword(SecurityUtils.encode(config.getCommon().getDefaultPwd()));
			} else {
				assertOriginalPassword(t.getPassword());
				t.setPassword(SecurityUtils.encode(t.getPassword()));
			}
		});
		// 检查重复用户名
		UserExample example = new UserExample();
		example.createCriteria().andUsernameIn(usernames);
		List<User> repeatUsernames = userMapper.selectByExample(example);
		if (ListUtils.isNotBlank(repeatUsernames)) {
			usernames.clear();
			for (User user : repeatUsernames) {
				usernames.add(user.getUsername());
			}
			throw new ResultException("用户名:" + String.join(",", usernames) + "已经存在,请修改后重新导入!");
		}
		// 检查重复邮箱
		example.clear();
		example.createCriteria().andEmailIn(emails);
		List<User> repeatEmails = userMapper.selectByExample(example);
		if (ListUtils.isNotBlank(repeatEmails)) {
			emails.clear();
			for (User user : repeatEmails) {
				emails.add(user.getEmail());
			}
			throw new ResultException("邮箱:" + String.join(",", emails) + "已经存在,请修改后重新导入!");
		}
		// 检查重复手机号
		example.clear();
		example.createCriteria().andMobileIn(mobiles);
		List<User> repeatMobiles = userMapper.selectByExample(example);
		if (ListUtils.isNotBlank(repeatMobiles)) {
			mobiles.clear();
			for (User user : repeatMobiles) {
				mobiles.add(user.getMobile());
			}
			throw new ResultException("手机号:" + String.join(",", mobiles) + "已经存在,请修改后重新导入!");
		}
	}
}