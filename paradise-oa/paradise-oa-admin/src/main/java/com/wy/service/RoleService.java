package com.wy.service;

import java.util.List;

import com.wy.base.BaseService;
import com.wy.model.Role;

/**
 * 角色表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
public interface RoleService extends BaseService<Role, Long> {

	/**
	 * 根据用户ID获取角色选择框列表
	 * 
	 * @param userId 用户ID
	 * @return 选中角色ID列表
	 */
	List<Role> getByUserId(Long userId);
}