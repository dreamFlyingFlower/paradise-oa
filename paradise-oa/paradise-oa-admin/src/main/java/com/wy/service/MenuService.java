package com.wy.service;

import java.util.List;
import java.util.Set;

import com.wy.base.BaseService;
import com.wy.base.Tree;
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
	 * @apiNote 根据上级编号查询上级以及直接下级信息
	 * @param menuId 上级菜单编号
	 * @return 上级以及直接下级信息list
	 */
	// List<Map<String, Object>> getSelfChildren(int menuId);
	/**
	 * 根据用户查询系统菜单列表
	 * 
	 * @param userId 用户ID
	 * @return 菜单列表
	 */
	List<Menu> selectMenuList(Long userId);

	/**
	 * 根据用户查询系统菜单列表
	 * 
	 * @param menu 菜单信息
	 * @param userId 用户ID
	 * @return 菜单列表
	 */
	List<Menu> selectMenuList(Menu menu, Long userId);

	/**
	 * 根据用户ID查询权限
	 * 
	 * @param userId 用户ID
	 * @return 权限列表
	 */
	Set<String> selectMenuPermsByUserId(Long userId);

	/**
	 * 根据用户ID查询菜单树信息
	 * 
	 * @param userId 用户ID
	 * @return 菜单列表
	 */
	List<Menu> selectMenuTreeByUserId(Long userId);

	/**
	 * 根据角色ID查询菜单树信息
	 * 
	 * @param roleId 角色ID
	 * @return 选中菜单列表
	 */
	List<Integer> selectMenuListByRoleId(Long roleId);

	/**
	 * 构建前端路由所需要的菜单
	 * 
	 * @param menus 菜单列表
	 * @return 路由列表
	 */
	List<RouterVo> buildMenus(List<Menu> menus);

	/**
	 * 构建前端所需要树结构
	 * 
	 * @param menus 菜单列表
	 * @return 树结构列表
	 */
	List<Menu> buildMenuTree(List<Menu> menus);

	/**
	 * 构建前端所需要下拉树结构
	 * 
	 * @param menus 菜单列表
	 * @return 下拉树结构列表
	 */
	List<Tree<Menu, Long>> buildMenuTreeSelect(List<Menu> menus);

	/**
	 * 是否存在菜单子节点
	 * 
	 * @param menuId 菜单ID
	 * @return 结果 true 存在 false 不存在
	 */
	boolean hasChildByMenuId(Long menuId);

	/**
	 * 查询菜单是否存在角色
	 * 
	 * @param menuId 菜单ID
	 * @return 结果 true 存在 false 不存在
	 */
	boolean checkMenuExistRole(Long menuId);

	/**
	 * 校验菜单名称是否唯一
	 * 
	 * @param menu 菜单信息
	 * @return 结果
	 */
	String checkMenuNameUnique(Menu menu);

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