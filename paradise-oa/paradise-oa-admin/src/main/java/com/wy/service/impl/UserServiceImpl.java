package com.wy.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.wy.base.AbstractService;
import com.wy.component.AsyncService;
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
	private MessageSource messageSource;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private FileinfoService fileinfoService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private MenuServiceImpl menuServiceImpl;

	/**
	 * 通过用户名,邮件,手机号查询用户信息
	 * 
	 * @param username 用户名或邮件或手机号
	 * @return 用户信息
	 */
	public User getByUsername(String username) {
		List<User> entitys = userMapper.selectByUsername(username);
		if (ListUtils.isBlank(entitys) || entitys.size() > 1) {
			return null;
		}
		return entitys.get(0);
	}

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
		String code = ServletUtils.getParameter("code");
		if (StrUtils.isBlank(code)) {
			throw new AuthException(TipEnum.TIP_LOGIN_FAIL_CODE_EMPTY);
		}
		String sessionCode = redisService.getStr(ServletUtils.getHttpServletRequest().getRequestedSessionId());
		if (StrUtils.isBlank(sessionCode)) {
			asyncService.recordLoginLog(username, TipEnum.TIP_RESPONSE_FAIL.getCode(),
					messageSource.getMessage("user.code.expire", null, Locale.getDefault()));
			throw new ResultException("验证码过期");
		}
		if (!Objects.equals(code, sessionCode)) {
			asyncService.recordLoginLog(username, CommonEnum.NO.ordinal(),
					messageSource.getMessage("user.code.error", null, Locale.getDefault()));
			throw new ResultException("验证码错误,请重新输入或刷新验证码");
		}
		// 验证过后删除session中的缓存
		// ServletUtils.getHttpSession().removeAttribute(Constants.REDIS_KEY_CAPTCHA_CODE);
		redisService.delete(ServletUtils.getHttpServletRequest().getRequestedSessionId());
	}

	/**
	 * 用户状态处理
	 * 
	 * @param user 用户信息
	 */
	private void handlerState(User user) {
		if (UserState.USER_BLACK.getCode() == user.getState()) {
			log.info("登录用户:{}是黑名单用户", user.getUsername());
			throw new AuthException("对不起,账号:" + user.getUsername() + "是黑名单帐号,请联系客服");
		}
		if (UserState.USER_DELETE.getCode() == user.getState()) {
			log.info("登录用户:{}已被删除", user.getUsername());
			throw new AuthException("对不起,账号:" + user.getUsername() + "已被删除,请联系管理员");
		}
		if (UserState.USER_LOCK.getCode() == user.getState()) {
			log.info("登录用户:{}为锁定状态", user.getUsername());
			throw new AuthException("对不起,账号:" + user.getUsername() + "被锁定,请等待" + config.getCommon());
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
	 * 通过用户名查询用户
	 * 
	 * @param username 用户名
	 * @return 用户对象信息
	 */
	@Override
	public User selectByUsername(String username) {
		return getByUsername(username);
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
		// 这个是表单中输入的密码,密码的形式为:加密(密码_当前时间戳)
		String password = user.getPassword();
		if (StrUtils.isBlank(password)) {
			password = config.getCommon().getDefaultPwd();
		} else {
			String temp_pwd = CryptoUtils.AESSimpleCrypt(password, config.getLogin().getSecretKeyUser(), false);
			password = temp_pwd.substring(0, temp_pwd.lastIndexOf("_"));
			if (password.length() > 12) {
				throw new ResultException("密码长度不能超过12位");
			}
		}
		// 使用该加密方式是spring推荐,加密后的长度为60,且被加密的字符串不得超过72
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(password));
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

	@Override
	public Object getById(Long userId) {
		User user = userMapper.selectByPrimaryKey(userId);
		user.setUserinfo(userinfoMapper.selectByPrimaryKey(userId));
		if (!Objects.isNull(user.getDepartId())) {
			user.setDepart(departMapper.selectByPrimaryKey(user.getDepartId()));
		}
		List<Role> roles = roleMapper.selectByUserId(userId);
		user.setRoles(roles);
		user.setRoleIds(roles.stream().map(t -> t.getRoleId()).collect(Collectors.toList()));
		return user;
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

	/**
	 * 重置用户密码
	 * 
	 * @param userName 用户名
	 * @param password 密码
	 * @return 结果
	 */
	@Override
	public int resetUserPwd(Long userId, String password) {
		return userMapper.updateByPrimaryKeySelective(User.builder().userId(userId).password(password).build());
	}

	/**
	 * 新增用户角色信息
	 * 
	 * @param user 用户对象
	 */
	public void insertUserRole(User user) {
		List<Long> roles = user.getRoleIds();
		if (ListUtils.isNotBlank(roles)) {
			// 新增用户与角色管理
			List<UserRole> list = new ArrayList<UserRole>();
			for (Long roleId : roles) {
				UserRole ur = new UserRole();
				ur.setUserId(user.getUserId());
				ur.setRoleId(roleId);
				list.add(ur);
			}
			if (list.size() > 0) {
				userRoleMapper.inserts(list);
			}
		}
	}

	/**
	 * 通过用户ID删除用户
	 * 
	 * @param userId 用户ID
	 * @return 结果
	 */
	@Override
	public int delete(Long userId) {
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
		for (Long userId : userIds) {
			assertModifyed(User.builder().userId(userId).build());
		}
		return userMapper.deleteByPrimaryKeys(userIds);
	}

	/**
	 * 导入用户数据
	 * 
	 * @param userList 用户数据列表
	 * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
	 * @param operName 操作用户
	 * @return 结果
	 */
	@Override
	public String importUser(List<User> userList, Boolean isUpdateSupport, String operName) {
		if (Objects.isNull(userList) || userList.size() == 0) {
			throw new ResultException("导入用户数据不能为空！");
		}
		int successNum = 0;
		int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		for (User user : userList) {
			try {
				// 验证是否存在这个用户
				User u = getByUsername(user.getUsername());
				if (Objects.isNull(u)) {
					user.setPassword(SecurityUtils.encode(config.getCommon().getDefaultPwd()));
					this.insertSelective(user);
					successNum++;
					successMsg.append("<br/>" + successNum + "、账号 " + user.getUsername() + " 导入成功");
				} else if (isUpdateSupport) {
					this.insertSelective(user);
					successNum++;
					successMsg.append("<br/>" + successNum + "、账号 " + user.getUsername() + " 更新成功");
				} else {
					failureNum++;
					failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUsername() + " 已存在");
				}
			} catch (Exception e) {
				failureNum++;
				String msg = "<br/>" + failureNum + "、账号 " + user.getUsername() + " 导入失败：";
				failureMsg.append(msg + e.getMessage());
				log.error(msg, e);
			}
		}
		if (failureNum > 0) {
			failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
			throw new ResultException(failureMsg.toString());
		} else {
			successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
		}
		return successMsg.toString();
	}
}