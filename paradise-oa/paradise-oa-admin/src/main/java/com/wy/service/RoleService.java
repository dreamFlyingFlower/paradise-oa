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

	// /**
	// * 根据角色权限查询菜单树,不查询按钮
	// * @param roleId 角色编号
	// * @return
	// */
	// List<Map<String, Object>> getRoleMenu(int roleId);
	//
	// /**
	// * 根据角色权限查询菜单树,以及菜单下所有按钮
	// * @param roleId 角色编号
	// * @return
	// */
	// List<Map<String, Object>> getRoleMenus(int roleId);
	//
	// /**
	// * 权限保存
	// * @param roleId 角色编号
	// * @param data key分别为menus,halfMenus,buttons,值是全选菜单编号,半选菜单编号列表,全选按钮编号
	// * {@link menus List<Integer>;halfMenus
	// * List<Integer>;buttons->List<Integer>}
	// */
	// void savePermissions(int roleId, Map<String, List<Integer>> datas);
	//
	// /**
	// * 权限保存
	// * @param roleId 角色编号
	// * @param data 需要保存的数据,其中每条数据的isCheck为1的时候表示保存
	// */
	// void saveMenus(int roleId, List<Map<String, Object>> datas);

	/**
	 * 根据用户ID获取角色选择框列表
	 * 
	 * @param userId 用户ID
	 * @return 选中角色ID列表
	 */
	List<Role> getByUserId(Long userId);

	/**
	 * 校验角色是否允许操作
	 * 
	 * @param role 角色信息
	 */
	void checkRoleAllowed(Role role);

	/**
	 * 修改角色状态
	 * 
	 * @param role 角色信息
	 * @return 结果
	 */
	int updateRoleStatus(Role role);

	/**
	 * 修改数据权限信息
	 * 
	 * @param role 角色信息
	 * @return 结果
	 */
	int authDataScope(Role role);
}