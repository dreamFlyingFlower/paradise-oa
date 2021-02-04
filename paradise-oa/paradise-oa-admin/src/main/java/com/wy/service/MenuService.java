package com.wy.service;

import java.util.List;

import com.wy.base.BaseService;
import com.wy.model.Menu;

/**
 * 菜单表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
public interface MenuService extends BaseService<Menu, Long> {

	/**
	 * 根据上级菜单编号获得本身以及直接下级菜单列表
	 * 
	 * @param id 本级菜单编号
	 * @return 本级菜单和直接下次菜单列表
	 */
	List<Menu> getSelfChildren(Long id);

	/**
	 * 根据用户ID查询菜单树信息
	 * 
	 * @param userId 用户ID
	 * @return 菜单列表
	 */
	List<Menu> getTreeByUserId(Long userId);

	/**
	 * 根据角色ID查询菜单树信息
	 * 
	 * @param roleId 角色ID
	 * @return 选中菜单列表
	 */
	List<Menu> getTreeByRoleId(Long roleId);
}