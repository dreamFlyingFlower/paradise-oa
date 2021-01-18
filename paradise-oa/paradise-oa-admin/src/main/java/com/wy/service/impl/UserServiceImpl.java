package com.wy.service.impl;

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

import com.github.pagehelper.PageInfo;
import com.wy.base.AbstractService;
import com.wy.crypto.CryptoUtils;
import com.wy.enums.BooleanEnum;
import com.wy.enums.TipEnum;
import com.wy.enums.UserState;
import com.wy.exception.AuthException;
import com.wy.manager.AsyncTask;
import com.wy.mapper.RoleMapper;
import com.wy.mapper.UserMapper;
import com.wy.mapper.UserRoleMapper;
import com.wy.model.Role;
import com.wy.model.User;
import com.wy.model.UserRole;
import com.wy.properties.ConfigProperties;
import com.wy.result.Result;
import com.wy.result.ResultException;
import com.wy.service.MenuService;
import com.wy.service.UserService;
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
	private UserRoleMapper userRoleMapper;

	@Autowired
	private ConfigProperties config;

	@Autowired
	private MenuService menuService;

	@Autowired
	private AsyncTask asyncTask;

	@Autowired
	private MessageSource messageSource;

	/**
	 * spring security登录校验,除了用户名和密码之外,其他参数从request工具中拿
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (config.getApi().isValidCode()) {
			String code = ServletUtils.getParameter("code");
			if (StrUtils.isBlank(code)) {
				throw new AuthException("验证码不能为空");
			}
			String sessionCode = ServletUtils.getHttpSession().getAttribute(config.getCache().getIdentifyCodeKey())
					.toString();
			if (StrUtils.isBlank(sessionCode)) {
				asyncTask.recordLogininfo(username, TipEnum.TIP_RESPONSE_FAIL.getCode(),
						messageSource.getMessage("user.code.expire", null, Locale.getDefault()));
				throw new ResultException("验证码过期");
			}
			if (!Objects.equals(code, sessionCode)) {
				asyncTask.recordLogininfo(username, BooleanEnum.NO.ordinal(),
						messageSource.getMessage("user.code.error", null, Locale.getDefault()));
				throw new ResultException("验证码错误,请重新输入或刷新验证码");
			}
			// 验证过后删除session中的缓存
			ServletUtils.getHttpSession().removeAttribute(config.getCache().getIdentifyCodeKey());
		}
		User user = userMapper.selectByUsername(username);
		if (Objects.isNull(user)) {
			log.info("登录用户:{}不存在.", username);
			throw new AuthException("对不起,帐号:" + username + " 不存在");
		} else if (UserState.USER_DELETE.getCode() == user.getState()) {
			log.info("登录用户:{}已被删除.", username);
			throw new ResultException("对不起,账号:" + username + " 已被删除");
		} else if (UserState.USER_BLACK.getCode() == user.getState()) {
			log.info("登录用户:{}是黑名单用户.", username);
			throw new ResultException("对不起,账号:" + username + "是黑名单帐号,请联系客服");
		}
		user.setPermissions(menuService.getMenuPermission(user.getUserId()));
		return user;
	}

	/**
	 * 根据条件分页查询用户列表 FIXME
	 * 
	 * @param user 用户信息
	 * @return 用户信息集合信息
	 */
	@Override
	public Result<List<User>> selectUserList(User user) {
		startPage(user);
		List<User> list = userMapper.selectEntitys(user);
		PageInfo<User> pageInfo = new PageInfo<>(list);
		return Result.page(list, pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal());
	}

	/**
	 * 通过用户名查询用户
	 * 
	 * @param userName 用户名
	 * @return 用户对象信息
	 */
	@Override
	public User selectByUsername(String username) {
		return userMapper.selectByUsername(username);
	}

	/**
	 * 查询用户所属角色组
	 * 
	 * @param userName 用户名
	 * @return 结果
	 */
	@Override
	public String selectUserRoleGroup(String userName) {
		List<Role> list = roleMapper.selectRolesByUserName(userName);
		StringBuffer idsStr = new StringBuffer();
		for (Role role : list) {
			idsStr.append(role.getRoleName()).append(",");
		}
		if (StrUtils.isNotBlank(idsStr.toString())) {
			return idsStr.substring(0, idsStr.length() - 1);
		}
		return idsStr.toString();
	}

	/**
	 * 校验用户是否允许操作
	 * 
	 * @param user 用户信息
	 */
	public void checkUserAllowed(User user) {
		if (Objects.nonNull(user.getUserId()) && user.getUserId().intValue() == 1) {
			throw new ResultException("不允许操作超级管理员用户");
		}
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
			String temp_pwd = CryptoUtils.AESSimpleCrypt(password, config.getCommon().getSecretKeyUser(), false);
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
		checkUserAllowed(user);
		Long userId = user.getUserId();
		// 删除用户与角色关联
		userRoleMapper.deleteUserRoleByUserId(userId);
		// 新增用户与角色管理
		insertUserRole(user);
		return userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public Object getById(Long id) {
		User user = baseMapper.selectByPrimaryKey(id);
		user.setRoles(roleMapper.selectEntitys(new Role()));
		user.setRoleIds(roleMapper.selectByUserId(id).stream().map(t -> {
			return t.getRoleId();
		}).collect(Collectors.toList()));
		return user;
	}

	/**
	 * 修改用户状态
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	@Override
	public int updateUserStatus(User user) {
		return userMapper.updateByPrimaryKeySelective(user);
	}

	/**
	 * 修改用户基本信息
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	@Override
	public int updateUserProfile(User user) {
		return userMapper.updateByPrimaryKeySelective(user);
	}

	/**
	 * 修改用户头像
	 * 
	 * @param userId 用户ID
	 * @param avatar 头像地址
	 * @return 结果
	 */
	public boolean updateUserAvatar(String userName, String avatar) {
		return userMapper.updateUserAvatar(userName, avatar) > 0;
	}

	/**
	 * 重置用户密码
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	@Override
	public int resetPwd(User user) {
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
	public int resetUserPwd(String userName, String password) {
		return userMapper.resetUserPwd(userName, password);
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
				userRoleMapper.batchUserRole(list);
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
		userRoleMapper.deleteUserRoleByUserId(userId);
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
			checkUserAllowed(User.builder().userId(userId).build());
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
				User u = userMapper.selectByUsername(user.getUsername());
				if (Objects.isNull(u)) {
					user.setPassword(SecurityUtils.encryptPassword(config.getCommon().getDefaultPwd()));
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