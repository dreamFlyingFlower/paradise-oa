package com.wy.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wy.base.BaseService;
import com.wy.model.Menu;
import com.wy.model.vo.RouterVo;

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
	 * 根据用户查询系统菜单列表
	 * 
	 * @param userId 用户ID
	 * @return 菜单列表
	 */
	List<Menu> getByUserId(Long userId);

	/**
	 * 根据用户ID查询菜单树信息
	 * 
	 * @param userId 用户ID
	 * @return 菜单列表
	 */
	List<Menu> getTreeByUserId(Long userId);

	/**
	 * 根据用户ID查询权限
	 * 
	 * @param userId 用户ID
	 * @return 权限列表
	 */
	Set<String> getPermsByUserId(Long userId);

	/**
	 * 根据角色ID查询菜单树信息
	 * 
	 * @param roleId 角色ID
	 * @return 选中菜单列表
	 */
	Map<String, Object> getByRoleId(Long roleId);

	/**
	 * 构建前端路由所需要的菜单
	 * 
	 * @param menus 菜单列表
	 * @return 路由列表
	 */
	List<RouterVo> buildMenus(List<Menu> menus);

	/**
	 * 获取角色数据权限
	 * 
	 * @param user 用户信息
	 * @return 角色权限信息
	 */
	Set<String> getRolePermission(Long userId);

	/**
	 * 获取菜单数据权限
	 * 
	 * @param userId 用户编号
	 * @return 菜单权限信息
	 */
	Set<String> getMenuPermission(Long userId);
}