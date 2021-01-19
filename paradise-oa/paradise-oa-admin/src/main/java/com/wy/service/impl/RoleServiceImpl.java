package com.wy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wy.base.AbstractService;
import com.wy.mapper.RoleDepartMapper;
import com.wy.mapper.RoleMapper;
import com.wy.mapper.RoleMenuMapper;
import com.wy.mapper.UserRoleMapper;
import com.wy.model.Role;
import com.wy.model.RoleDepart;
import com.wy.model.RoleDepartExample;
import com.wy.model.RoleMenu;
import com.wy.model.RoleMenuExample;
import com.wy.model.UserRole;
import com.wy.result.ResultException;
import com.wy.service.RoleService;
import com.wy.utils.ListUtils;

/**
 * 角色表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Service("roleService")
public class RoleServiceImpl extends AbstractService<Role, Long> implements RoleService {

	@Autowired
	private RoleMapper roleMapper;

	/**
	 * 根据用户编号判断是否为超级管理员,只考虑单角色
	 * 
	 * @param userId 用户编号
	 * @return true是,false否
	 */
	public boolean ifAdminUser(Long userId) {
		List<Role> selectEntitys = roleMapper.selectByUserId(userId);
		if (ListUtils.isBlank(selectEntitys)) {
			throw new ResultException("this user does not have assign role");
		}
		return selectEntitys.get(0).getRoleType() == 0;
	}

	/**
	 * 判断是否为超级管理员
	 * 
	 * @param roleId 角色编号
	 * @return true是,false否
	 */
	public boolean ifAdmin(Long roleId) {
		Role role = roleMapper.selectByPrimaryKey(roleId);
		if (null == role) {
			throw new ResultException("this role does not exist");
		}
		return role.getRoleType() == 0;
	}
	//
	// @Override
	// public List<Map<String, Object>> getRoleMenu(int roleId) {
	// boolean isSuperAdmin = ifAdmin(roleId);
	// List<Map<String, Object>> rootChildren = getChildrenMenu(isSuperAdmin,
	// roleId, 1);
	// if (ListUtils.isBlank(rootChildren)) {
	// throw new ResultException("该角色没有分配菜单!");
	// }
	// getRoleMenu(isSuperAdmin, roleId, rootChildren);
	// return rootChildren;
	// }
	//
	// private void getRoleMenu(boolean isSuperAdmin, int roleId,
	// List<Map<String, Object>> rootChildren) {
	// if (ListUtils.isBlank(rootChildren)) {
	// return;
	// }
	// for (Map<String, Object> child : rootChildren) {
	// if (Integer.parseInt(child.get("childNum").toString()) > 0) {
	// List<Map<String, Object>> children = getChildrenMenu(isSuperAdmin, roleId,
	// Integer.parseInt(child.get("menuId").toString()));
	// getRoleMenu(isSuperAdmin, roleId, children);
	// child.put("children", children);
	// }
	// }
	// }
	//
	// /**
	// * 根据角色查询菜单子菜单
	// */
	// private List<Map<String, Object>> getChildrenMenu(boolean isSuperAdmin, int
	// roleId,
	// int menuId) {
	// StringBuilder sb = new StringBuilder("select c.menu_id menuId,c.menu_name
	// menuName,")
	// .append("c.router_name routerName,c.menu_icon menuIcon,c.menu_i18n
	// menuI18n,")
	// .append("c.menu_path menuPath,c.menu_view
	// menuView,c.pid,c.redirect,c.hidden,")
	// .append("(select count(*) from ti_menu where pid = c.menu_id) childNum ");
	// if (isSuperAdmin) {
	// return roleDao.getMaps(
	// Sqls.create(sb.append(" from ti_menu c where c.pid = @menuId").toString())
	// .setParam("menuId", menuId));
	// } else {
	// sb.append(" from ti_role a inner join tr_role_menu b on a.role_id = b.role_id
	// ")
	// .append(" inner join ti_menu c on c.menu_id = b.menu_id ")
	// .append(" where a.role_id = @roleId and c.pid = @menuId");
	// return roleDao.getMaps(Sqls.create(sb.toString()).setParam("roleId", roleId)
	// .setParam("menuId", menuId));
	// }
	// }
	//
	// /**
	// * 根据角色编号获得所管辖的菜单和按钮列表
	// */
	// @Override
	// public List<Map<String, Object>> getRoleMenus(int roleId) {
	// // 是否是超级管理员
	// boolean isSuperAdmin = ifAdmin(roleId);
	// List<Map<String, Object>> menus = getChildrenMenus(isSuperAdmin, roleId, 0);
	// if (ListUtils.isBlank(menus)) {
	// throw new ResultException("该角色没有分配菜单!");
	// }
	// addChildMenu(isSuperAdmin, roleId, menus);
	// return menus;
	// }
	//
	// /**
	// * 根据角色查询菜单子菜单以及按钮
	// * @param roleId 角色编号
	// * @param menuId 菜单编号
	// */
	// private List<Map<String, Object>> getChildrenMenus(boolean isSuperAdmin, int
	// roleId,
	// int menuId) {
	// StringBuilder sb = new StringBuilder("select a.menu_id menuId,a.menu_name
	// menuName,")
	// .append("(select count(*) from ti_menu c where c.pid = a.menu_id) childNum,")
	// .append("(select count(*) from ti_button b where b.menu_id = a.menu_id)
	// btnNum,");
	// if (isSuperAdmin) {
	// return roleDao.getMaps(Sqls.create(
	// sb.append("1 isCheck from ti_menu a where a.pid=@menuId order by a.sort")
	// .toString())
	// .setParam("menuId", menuId));
	// } else {
	// sb.append(
	// "ifnull((select menu_state from tr_role_menu d where d.menu_id = a.menu_id
	// and d.role_id = @roleId), 0)")
	// .append(" isCheck from ti_menu a where a.pid = @menuId order by a.sort");
	// return roleDao.getMaps(Sqls.create(sb.toString()).setParam("roleId", roleId)
	// .setParam("menuId", menuId));
	// }
	// }
	//
	// private void addChildMenu(boolean isSuperAdmin, int roleId, List<Map<String,
	// Object>> maps) {
	// if (ListUtils.isBlank(maps)) {
	// return;
	// }
	// for (Map<String, Object> map : maps) {
	// int menuId = NumberUtils.toInt(String.valueOf(map.get("menuId")));
	// if (ObjUtils.positiveNum(map.get("btnNum"))) {
	// map.put("button", getMenuButton(roleId, menuId));
	// }
	// if (ObjUtils.positiveNum(map.get("childNum"))) {
	// List<Map<String, Object>> childs = getChildrenMenus(isSuperAdmin, roleId,
	// menuId);
	// addChildMenu(isSuperAdmin, roleId, childs);
	// map.put("children", childs);
	// }
	// }
	// }
	//
	// /**
	// * 查询权限按钮
	// * @param roleId 角色编号
	// * @param menuId 菜单编号
	// */
	// private List<Map<String, Object>> getMenuButton(int roleId, int menuId) {
	// return roleDao.getMaps(Sqls.create(StrUtils.formatBuilder(
	// "select b.button_id buttonId,b.button_name buttonName,ifnull(",
	// "(select 1 from tr_role_button a where a.button_id = b.button_id and
	// a.role_id = @roleId),0",
	// " ) isCheck from ti_button b where b.menu_id = @menuId order by
	// b.button_id"))
	// .setParam("roleId", roleId).setParam("menuId", menuId));
	// }
	//
	// /**
	// * 权限保存
	// * @param roleId 角色编号
	// * @param data 需要保存的数据,其中每条数据的isCheck为1的时候表示保存
	// */
	// @Transactional
	// public void savePermissions(int roleId, Map<String, List<Integer>> datas) {
	// if (getById(roleId) == null) {
	// throw new ResultException("不存在这个角色");
	// }
	// getBaseDao().clear(RoleMenuOld.class, Cnd.where(Exps.eq("role_id", roleId)));
	// getBaseDao().clear(RoleButtonOld.class, Cnd.where(Exps.eq("role_id",
	// roleId)));
	// savePermission(roleId, datas);
	// }
	//
	// private void savePermission(int roleId, Map<String, List<Integer>> datas) {
	// if (MapUtils.isBlank(datas)) {
	// return;
	// }
	// // 全选状态菜单
	// List<Integer> menuIds = datas.get("menus");
	// if (ListUtils.isNotBlank(menuIds)) {
	// List<RoleMenuOld> roleMenus = new ArrayList<>();
	// for (int menuId : menuIds) {
	// RoleMenuOld temp =
	// RoleMenuOld.builder().roleId(roleId).menuId(menuId).menuState(1)
	// .build();
	// roleMenus.add(temp);
	// }
	// getBaseDao().fastInsert(roleMenus);
	// }
	// // 半选状态菜单
	// List<Integer> halfMenuIds = datas.get("halfMenus");
	// if (ListUtils.isNotBlank(halfMenuIds)) {
	// List<RoleMenuOld> roleHalfMenus = new ArrayList<>();
	// for (int halfMenuId : halfMenuIds) {
	// RoleMenuOld temp =
	// RoleMenuOld.builder().roleId(roleId).menuId(halfMenuId).menuState(2)
	// .build();
	// roleHalfMenus.add(temp);
	// }
	// getBaseDao().fastInsert(roleHalfMenus);
	// }
	// // 按钮
	// List<Integer> buttonIds = datas.get("buttons");
	// if (ListUtils.isNotBlank(buttonIds)) {
	// List<RoleButtonOld> roleButtons = new ArrayList<>();
	// for (int buttonId : buttonIds) {
	// RoleButtonOld temp =
	// RoleButtonOld.builder().roleId(roleId).buttonId(buttonId).build();
	// roleButtons.add(temp);
	// }
	// getBaseDao().fastInsert(roleButtons);
	// }
	// }
	//
	// /**
	// * 权限保存
	// * @param roleId 角色编号
	// * @param data 需要保存的数据,其中每条数据的isCheck为1的时候表示保存
	// */
	// @Transactional
	// public void saveMenus(int roleId, List<Map<String, Object>> datas) {
	// if (getById(roleId) == null) {
	// throw new ResultException("不存在这个角色");
	// }
	// getBaseDao().clear(RoleMenuOld.class, Cnd.where(Exps.eq("role_id", roleId)));
	// getBaseDao().clear(RoleButtonOld.class, Cnd.where(Exps.eq("role_id",
	// roleId)));
	// saveMenu(roleId, datas);
	// }
	//
	// @SuppressWarnings("unchecked")
	// private void saveMenu(int roleId, List<Map<String, Object>> menus) {
	// if (ListUtils.isBlank(menus)) {
	// return;
	// }
	// for (Map<String, Object> menu : menus) {
	// if (Objects.isNull(menu.get("isCheck"))
	// || NumberUtils.toInt(menu.get("isCheck").toString()) == 0) {
	// continue;
	// }
	// RoleMenuOld rm = new RoleMenuOld();
	// rm.setRoleId(roleId);
	// rm.setMenuId(Integer.parseInt(menu.get("menuId").toString()));
	// getBaseDao().insert(rm);
	//
	// List<Map<String, Object>> buttons = (List<Map<String, Object>>)
	// menu.get("button");
	// if (ListUtils.isNotBlank(buttons)) {
	// for (Map<String, Object> btn : buttons) {
	// if (btn.get("isCheck") != null
	// && NumberUtils.isDigits(btn.get("isCheck").toString())
	// && NumberUtils.toInt(btn.get("isCheck").toString()) == 1) {
	// RoleButtonOld rb = new RoleButtonOld();
	// rb.setRoleId(roleId);
	// rb.setButtonId((Integer) btn.get("buttonId"));
	// getBaseDao().insert(rb);
	// }
	// }
	// }
	// List<Map<String, Object>> child = (List<Map<String, Object>>)
	// menu.get("children");
	// saveMenu(roleId, child);
	// }
	// }

	@Autowired
	private RoleMenuMapper roleMenuMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private RoleDepartMapper roleDepartMapper;

	/**
	 * 根据用户ID获取角色选择框列表
	 * 
	 * @param userId 用户ID
	 * @return 角色列表
	 */
	@Override
	public List<Role> getByUserId(Long userId) {
		return roleMapper.selectByUserId(userId);
	}

	/**
	 * 校验角色是否允许操作 FIXME
	 * 
	 * @param role 角色信息
	 */
	@Override
	public void checkRoleAllowed(Role role) {
		if (Objects.nonNull(role.getRoleId()) && role.getRoleId() == 1) {
			throw new ResultException("不允许操作超级管理员角色");
		}
	}

	/**
	 * 新增保存角色信息
	 * 
	 * @param role 角色信息
	 * @return 结果
	 */
	@Override
	public Object insertSelective(Role role) {
		roleMapper.insertSelective(role);
		return insertRoleMenu(role);
	}

	/**
	 * 修改保存角色信息
	 * 
	 * @param role 角色信息
	 * @return 结果
	 */
	@Override
	public int updateSelective(Role role) {
		checkRoleAllowed(role);
		// 修改角色信息
		roleMapper.updateByPrimaryKeySelective(role);
		// 删除角色与菜单关联
		RoleMenuExample example = new RoleMenuExample();
		example.createCriteria().andRoleIdEqualTo(role.getRoleId());
		roleMenuMapper.deleteByExample(example);
		return insertRoleMenu(role);
	}

	/**
	 * 修改角色状态
	 * 
	 * @param role 角色信息
	 * @return 结果
	 */
	public int updateRoleStatus(Role role) {
		return roleMapper.updateByPrimaryKeySelective(role);
	}

	/**
	 * 修改数据权限信息
	 * 
	 * @param role 角色信息
	 * @return 结果
	 */
	@Override
	@Transactional
	public int authDataScope(Role role) {
		// 修改角色信息
		roleMapper.updateByPrimaryKeySelective(role);
		// 删除角色与部门关联
		RoleDepartExample example = new RoleDepartExample();
		example.createCriteria().andRoleIdEqualTo(role.getRoleId());
		roleDepartMapper.deleteByExample(example);
		// 新增角色和部门信息（数据权限）
		return insertRoleDept(role);
	}

	/**
	 * 新增角色菜单信息
	 * 
	 * @param role 角色对象
	 */
	public int insertRoleMenu(Role role) {
		int rows = 1;
		// 新增用户与角色管理
		List<RoleMenu> list = new ArrayList<RoleMenu>();
		for (Long menuId : role.getMenuIds()) {
			RoleMenu rm = new RoleMenu();
			rm.setRoleId(role.getRoleId());
			rm.setMenuId(menuId);
			list.add(rm);
		}
		if (list.size() > 0) {
			roleMenuMapper.inserts(list);
		}
		return rows;
	}

	/**
	 * 新增角色部门信息(数据权限)
	 * 
	 * @param role 角色对象
	 */
	public int insertRoleDept(Role role) {
		int rows = 1;
		// 新增角色与部门（数据权限）管理
		List<RoleDepart> list = new ArrayList<RoleDepart>();
		for (Long deptId : role.getDepartIds()) {
			RoleDepart rd = new RoleDepart();
			rd.setRoleId(role.getRoleId());
			rd.setDepartId(deptId);
			list.add(rd);
		}
		if (list.size() > 0) {
			roleDepartMapper.inserts(list);
		}
		return rows;
	}

	/**
	 * 批量删除角色信息
	 * 
	 * @param roleIds 需要删除的角色ID
	 * @return 结果
	 */
	public int deleteRoleByIds(List<Long> roleIds) {
		for (Long roleId : roleIds) {
			checkRoleAllowed(Role.builder().roleId(roleId).build());
			Role role = baseMapper.selectByPrimaryKey(roleId);
			if (userRoleMapper.countByEntity(UserRole.builder().roleId(roleId).build()) > 0) {
				throw new ResultException(String.format("%1$s已分配,不能删除", role.getRoleName()));
			}
		}
		return super.deletes(roleIds);
	}
}