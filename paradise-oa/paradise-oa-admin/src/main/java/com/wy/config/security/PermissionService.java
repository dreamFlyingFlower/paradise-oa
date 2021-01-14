package com.wy.config.security;

import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.wy.model.Role;
import com.wy.model.User;
import com.wy.util.ServletUtils;
import com.wy.utils.StrUtils;

/**
 * 自定义权限实现 FIXME
 * 
 * @author ParadiseWY
 * @date 2020年4月5日 下午12:51:37
 */
@Service("ss")
public class PermissionService {

	/** 所有权限标识 */
	private static final String ALL_PERMISSION = "*:*:*";

	/** 管理员角色权限标识 */
	private static final int SUPER_ADMIN = 0;

	private static final String PERMISSION_DELIMETER = ",";

	@Autowired
	private TokenService tokenService;

	/**
	 * 验证用户是否具备某权限
	 * 
	 * @param permission 权限字符串
	 * @return 用户是否具备某权限
	 */
	public boolean hasPermi(String permission) {
		if (StrUtils.isBlank(permission)) {
			return false;
		}
		User loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
		if (Objects.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
			return false;
		}
		return hasPermissions(loginUser.getPermissions(), permission);
	}

	/**
	 * 验证用户是否不具备某权限，与 hasPermi逻辑相反
	 * 
	 * @param permission 权限字符串
	 * @return 用户是否不具备某权限
	 */
	public boolean lacksPermi(String permission) {
		return hasPermi(permission) != true;
	}

	/**
	 * 验证用户是否具有以下任意一个权限
	 * 
	 * @param permissions 以 PERMISSION_NAMES_DELIMETER 为分隔符的权限列表
	 * @return 用户是否具有以下任意一个权限
	 */
	public boolean hasAnyPermi(String permissions) {
		if (StrUtils.isBlank(permissions)) {
			return false;
		}
		User loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
		if (Objects.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
			return false;
		}
		Set<String> authorities = loginUser.getPermissions();
		for (String permission : permissions.split(PERMISSION_DELIMETER)) {
			if (permission != null && hasPermissions(authorities, permission)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断用户是否拥有某个角色
	 * 
	 * @param role 角色字符串
	 * @return 用户是否具备某角色
	 */
	public boolean hasRole(int role) {
		User loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
		if (Objects.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getRoles())) {
			return false;
		}
		for (Role sysRole : loginUser.getRoles()) {
			int roleKey = sysRole.getRoleType();
			if (SUPER_ADMIN == roleKey || roleKey == role) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证用户是否不具备某角色，与 isRole逻辑相反。
	 * 
	 * @param role 角色名称
	 * @return 用户是否不具备某角色
	 */
	public boolean lacksRole(int role) {
		return hasRole(role) != true;
	}

	/**
	 * 验证用户是否具有以下任意一个角色
	 * 
	 * @param roles 以 ROLE_NAMES_DELIMETER 为分隔符的角色列表
	 * @return 用户是否具有以下任意一个角色
	 */
	public boolean hasAnyRoles(int roles) {
		User loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
		if (Objects.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getRoles())) {
			return false;
		}
		if (hasRole(roles)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否包含权限
	 * 
	 * @param permissions 权限列表
	 * @param permission 权限字符串
	 * @return 用户是否具备某权限
	 */
	private boolean hasPermissions(Set<String> permissions, String permission) {
		return permissions.contains(ALL_PERMISSION) || permissions.contains(permission.trim());
	}
}