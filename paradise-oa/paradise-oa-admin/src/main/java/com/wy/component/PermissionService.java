package com.wy.component;

import java.util.List;
import java.util.Objects;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.wy.common.Constants;
import com.wy.enums.Permission;
import com.wy.model.Role;
import com.wy.model.User;
import com.wy.model.vo.PermissionVo;
import com.wy.util.SecurityUtils;
import com.wy.utils.StrUtils;

/**
 * 利用{@link PreAuthorize}的SpringEl表达式自定义权限实现
 * 
 * 自定义表示构成:角色1,角色2:权限,中间用:隔开;多个角色用逗号分开,权限只能是一种;如ADMIN,MANAGER:DELETE
 * 
 * @author 飞花梦影
 * @date 2021-01-20 20:06:26
 * @git {@link https://github.com/mygodness100}
 */
@Component("permissionService")
public class PermissionService {

	private User getLoginUser(String... param) {
		if (StrUtils.isBlank(param)) {
			return null;
		}
		User loginUser = SecurityUtils.getLoginUser();
		if (Objects.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissionVos())
				|| CollectionUtils.isEmpty(loginUser.getRoles())) {
			return null;
		}
		return loginUser;
	}

	/**
	 * 验证用户是否具备个权限
	 * 
	 * @param permission 权限字符串
	 * @return 用户是否具备某权限
	 */
	public boolean hasAuthority(String permission) {
		User loginUser = getLoginUser(permission);
		if (Objects.isNull(loginUser)) {
			return false;
		}
		return hasPermissions(loginUser.getPermissionVos(), permission);
	}

	/**
	 * 验证用户是否不具备某权限,与hasAuthority相反
	 * 
	 * @param permission 权限字符串
	 * @return 用户是否不具备某权限
	 */
	public boolean hasNotAuthority(String permission) {
		return !hasAuthority(permission);
	}

	/**
	 * 验证用户是否具有以下任意一个权限
	 * 
	 * @param permissions 权限列表
	 * @return 用户是否具有以下任意一个权限
	 */
	public boolean hasAnyAuthority(String... permissions) {
		User loginUser = getLoginUser(permissions);
		if (Objects.isNull(loginUser)) {
			return false;
		}
		List<PermissionVo> permissionVos = loginUser.getPermissionVos();
		for (String permission : permissions) {
			if (hasPermissions(permissionVos, permission)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否包含权限
	 * 
	 * @param permissions 用户权限列表
	 * @param permission 权限字符串
	 * @return 用户是否具备某权限
	 */
	private boolean hasPermissions(List<PermissionVo> permissions, String permission) {
		String[] roleAndPermissions = permission.split(":");
		if (StrUtils.isBlank(roleAndPermissions) || roleAndPermissions.length != 2) {
			return false;
		}
		if (StrUtils.isBlank(roleAndPermissions[0]) || StrUtils.isBlank(roleAndPermissions[1])) {
			return false;
		}
		// 权限数组
		String[] roleArray = roleAndPermissions[0].split(",");
		for (PermissionVo permissionVo : permissions) {
			// 超级管理员
			if (Constants.SUPER_ADMIN.equalsIgnoreCase(permissionVo.getRoleCode())) {
				return true;
			}
			for (String role : roleArray) {
				if (permissionVo.getRoleCode().equalsIgnoreCase(role) && (permissionVo.getPermissions().toLowerCase()
						.contains(Permission.ALL.name().toLowerCase())
						|| permissionVo.getPermissions().toLowerCase().contains(roleAndPermissions[1].toLowerCase()))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断用户是否拥有某个角色
	 * 
	 * @param roleCode 角色编码
	 * @return 用户是否具备某角色
	 */
	public boolean hasRole(String roleCode) {
		User loginUser = getLoginUser(roleCode);
		if (Objects.isNull(loginUser)) {
			return false;
		}
		for (Role sysRole : loginUser.getRoles()) {
			if (Constants.SUPER_ADMIN.equalsIgnoreCase(sysRole.getRoleCode())) {
				return true;
			}
			if (roleCode.equalsIgnoreCase(sysRole.getRoleCode())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证用户是否不具备某角色,与{@link #hasRole}逻辑相反
	 * 
	 * @param roleCode 角色编码
	 * @return 用户是否不具备某角色
	 */
	public boolean hasNotRole(String roleCode) {
		return !hasRole(roleCode);
	}

	/**
	 * 验证用户是否具有以下任意一个角色
	 * 
	 * @param roleCodes 角色列表
	 * @return 用户是否具有以下任意一个角色
	 */
	public boolean hasAnyRoles(String... roleCodes) {
		User loginUser = getLoginUser(roleCodes);
		if (Objects.isNull(loginUser)) {
			return false;
		}
		for (String roleCode : roleCodes) {
			for (Role sysRole : loginUser.getRoles()) {
				if (Constants.SUPER_ADMIN.equalsIgnoreCase(sysRole.getRoleCode())) {
					return true;
				}
				if (roleCode.equalsIgnoreCase(sysRole.getRoleCode())) {
					return true;
				}
			}
		}
		return false;
	}
}