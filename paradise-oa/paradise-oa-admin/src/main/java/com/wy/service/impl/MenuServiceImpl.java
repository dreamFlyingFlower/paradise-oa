package com.wy.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.base.AbstractService;
import com.wy.base.Tree;
import com.wy.common.UserConstants;
import com.wy.mapper.MenuMapper;
import com.wy.mapper.RoleMenuMapper;
import com.wy.model.Menu;
import com.wy.model.vo.MetaVo;
import com.wy.model.vo.RouterVo;
import com.wy.result.ResultException;
import com.wy.service.MenuService;
import com.wy.service.RoleService;
import com.wy.util.SecurityUtils;
import com.wy.utils.StrUtils;

/**
 * 菜单表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Service("menuService")
public class MenuServiceImpl extends AbstractService<Menu, Long> implements MenuService {

	@Autowired
	private MenuMapper menuMapper;

	@Autowired
	private RoleService roleService;

	// @Override
	// public List<Map<String, Object>> getSelfChildren(int menuId) {
	// return absDao.getMaps(Sqls
	// .create(StrUtils.formatBuilder(
	// "select menu_id menuId,menu_name menuName,menu_path menuPath,",
	// "menu_view menuView,router_name routerName,pid from ti_menu ",
	// "where pid = @menuId or menu_id = @menuId order by menu_id"))
	// .setParam("menuId", menuId));
	// }
	//
	// /**
	// * 查询本级菜单信息或直接下级菜单信息
	// * @param id 菜单编号
	// * @param parent 是否为上级菜单编号,true是,false否
	// * @return 本级或直接下级菜单列表
	// */
	// @Override
	// public List<Map<String, Object>> getTree(int id, boolean parent) {
	// StringBuffer sql = new StringBuffer("select a.menu_id
	// treeId,a.pid,a.menu_i18n menuI18n,")
	// .append("a.menu_name treeName,a.router_name routerName,a.menu_icon
	// menuIcon,a.sort,")
	// .append("a.menu_path menuPath,a.menu_view menuView,")
	// .append("(select count(*) from ti_menu where pid = a.menu_id) childNum ")
	// .append(" from ti_menu a where ").append(parent ? " a.pid=@id " : "a.menu_id
	// = @id")
	// .append(" order by a.sort");
	// return absDao.getMaps(Sqls.create(sql.toString()).setParam("id", id));
	// }

	public static final String PREMISSION_STRING = "perms[\"{0}\"]";

	@Autowired
	private RoleMenuMapper roleMenuMapper;

	/**
	 * 根据用户查询系统菜单列表
	 * 
	 * @param userId 用户ID
	 * @return 菜单列表
	 */
	@Override
	public List<Menu> selectMenuList(Long userId) {
		return selectMenuList(new Menu(), userId);
	}

	/**
	 * 查询系统菜单列表
	 * 
	 * @param menu 菜单信息
	 * @return 菜单列表
	 */
	@Override
	public List<Menu> selectMenuList(Menu menu, Long userId) {
		List<Menu> menuList = null;
		// 管理员显示所有菜单信息 FIXME
		if (userId == 1) {
			menuList = menuMapper.selectEntitys(menu);
		} else {
			menu.getParams().put("userId", userId);
			menuList = menuMapper.selectMenuListByUserId(menu);
		}
		return menuList;
	}

	/**
	 * 根据用户ID查询权限
	 * 
	 * @param userId 用户ID
	 * @return 权限列表
	 */
	@Override
	public Set<String> selectMenuPermsByUserId(Long userId) {
		List<String> perms = menuMapper.selectMenuPermsByUserId(userId);
		Set<String> permsSet = new HashSet<>();
		for (String perm : perms) {
			if (StrUtils.isNotBlank(perm)) {
				permsSet.addAll(Arrays.asList(perm.trim().split(",")));
			}
		}
		return permsSet;
	}

	/**
	 * 根据用户ID查询菜单
	 * 
	 * @param userId 用户名称
	 * @return 菜单列表
	 */
	@Override
	public List<Menu> selectMenuTreeByUserId(Long userId) {
		List<Menu> menus = null;
		if (SecurityUtils.isAdmin(userId)) {
			menus = menuMapper.selectMenuTreeAll();
		} else {
			menus = menuMapper.selectMenuTreeByUserId(userId);
		}
		return getChildPerms(menus, 0);
	}

	/**
	 * 根据角色ID查询菜单树信息
	 * 
	 * @param roleId 角色ID
	 * @return 选中菜单列表
	 */
	public List<Integer> selectMenuListByRoleId(Long roleId) {
		return menuMapper.selectMenuListByRoleId(roleId);
	}

	/**
	 * 构建前端路由所需要的菜单
	 * 
	 * @param menus 菜单列表
	 * @return 路由列表
	 */
	@Override
	public List<RouterVo> buildMenus(List<Menu> menus) {
		List<RouterVo> routers = new LinkedList<RouterVo>();
		for (Menu menu : menus) {
			RouterVo router = new RouterVo();
			router.setName(StringUtils.capitalize(menu.getMenuPath()));
			router.setPath(getRouterPath(menu));
			router.setComponent(StrUtils.isBlank(menu.getMenuView()) ? "Layout" : menu.getMenuView());
			router.setMeta(new MetaVo(menu.getMenuName(), menu.getMenuIcon()));
			List<Menu> cMenus = menu.getChildren();
			if (!cMenus.isEmpty() && cMenus.size() > 0) {
				router.setAlwaysShow(true);
				router.setRedirect("noRedirect");
				router.setChildren(buildMenus(cMenus));
			}
			routers.add(router);
		}
		return routers;
	}

	/**
	 * 构建前端所需要树结构
	 * 
	 * @param menus 菜单列表
	 * @return 树结构列表
	 */
	@Override
	public List<Menu> buildMenuTree(List<Menu> menus) {
		List<Menu> returnList = new ArrayList<Menu>();
		for (Iterator<Menu> iterator = menus.iterator(); iterator.hasNext();) {
			Menu t = iterator.next();
			// 根据传入的某个父节点ID,遍历该父节点的所有子节点
			if (t.getPid() == 0l) {
				recursionFn(menus, t);
				returnList.add(t);
			}
		}
		if (returnList.isEmpty()) {
			returnList = menus;
		}
		return returnList;
	}

	/**
	 * 构建前端所需要下拉树结构
	 * 
	 * @param menus 菜单列表
	 * @return 下拉树结构列表
	 */
	@Override
	public List<Tree<Menu, Long>> buildMenuTreeSelect(List<Menu> menus) {
		List<Menu> menuTrees = buildMenuTree(menus);
		return menuTrees.stream().map(Tree::new).collect(Collectors.toList());
	}

	/**
	 * 是否存在菜单子节点
	 * 
	 * @param menuId 菜单ID
	 * @return 结果
	 */
	@Override
	public boolean hasChildByMenuId(Long menuId) {
		int result = menuMapper.hasChildByMenuId(menuId);
		return result > 0 ? true : false;
	}

	/**
	 * 查询菜单使用数量
	 * 
	 * @param menuId 菜单ID
	 * @return 结果
	 */
	@Override
	public boolean checkMenuExistRole(Long menuId) {
		int result = roleMenuMapper.checkMenuExistRole(menuId);
		return result > 0 ? true : false;
	}

	/**
	 * 校验菜单名称是否唯一
	 * 
	 * @param menu 菜单信息
	 * @return 结果
	 */
	@Override
	public String checkMenuNameUnique(Menu menu) {
		Long menuId = Objects.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
		Menu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getPid());
		if (Objects.nonNull(info) && info.getMenuId().longValue() != menuId.longValue()) {
			return UserConstants.NOT_UNIQUE;
		}
		return UserConstants.UNIQUE;
	}

	/**
	 * 获取路由地址
	 * 
	 * @param menu 菜单信息
	 * @return 路由地址
	 */
	public String getRouterPath(Menu menu) {
		String routerPath = menu.getMenuPath();
		// 非外链并且是一级目录
		if (0 == menu.getPid() && 0 == menu.getLink()) {
			routerPath = "/" + menu.getMenuPath();
		}
		return routerPath;
	}

	/**
	 * 根据父节点的ID获取所有子节点
	 * 
	 * @param list 分类表
	 * @param parentId 传入的父节点ID
	 * @return String
	 */
	public List<Menu> getChildPerms(List<Menu> list, int parentId) {
		List<Menu> returnList = new ArrayList<Menu>();
		for (Iterator<Menu> iterator = list.iterator(); iterator.hasNext();) {
			Menu t = iterator.next();
			// 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
			if (t.getPid() == parentId) {
				recursionFn(list, t);
				returnList.add(t);
			}
		}
		return returnList;
	}

	/**
	 * 递归列表
	 * 
	 * @param list
	 * @param t
	 */
	private void recursionFn(List<Menu> list, Menu t) {
		// 得到子节点列表
		List<Menu> childList = getChildList(list, t);
		t.setChildren(childList);
		for (Menu tChild : childList) {
			if (hasChild(list, tChild)) {
				// 判断是否有子节点
				Iterator<Menu> it = childList.iterator();
				while (it.hasNext()) {
					Menu n = it.next();
					recursionFn(list, n);
				}
			}
		}
	}

	/**
	 * 得到子节点列表
	 */
	private List<Menu> getChildList(List<Menu> list, Menu t) {
		List<Menu> tlist = new ArrayList<Menu>();
		Iterator<Menu> it = list.iterator();
		while (it.hasNext()) {
			Menu n = it.next();
			if (n.getPid().longValue() == t.getMenuId().longValue()) {
				tlist.add(n);
			}
		}
		return tlist;
	}

	/**
	 * 判断是否有子节点
	 */
	private boolean hasChild(List<Menu> list, Menu t) {
		return getChildList(list, t).size() > 0 ? true : false;
	}

	@Override
	public int delete(Long id) {
		if (hasChildByMenuId(id)) {
			throw new ResultException("存在子菜单,不允许删除");
		}
		if (checkMenuExistRole(id)) {
			throw new ResultException("菜单已分配,不允许删除");
		}
		return super.delete(id);
	}

	@Override
	public int deletes(List<Long> ids) {
		int row = 0;
		for (Long id : ids) {
			row += delete(id);
		}
		return row;
	}

	/**
	 * 获取菜单数据权限
	 * 
	 * @param user 用户信息
	 * @return 菜单权限信息
	 */
	@Override
	public Set<String> getMenuPermission(Long userId) {
		Set<String> perms = new HashSet<String>();
		// 管理员拥有所有权限
		if (1 == userId) {
			perms.add("*:*:*");
		} else {
			perms.addAll(selectMenuPermsByUserId(userId));
		}
		return perms;
	}

	/**
	 * 获取菜单数据权限
	 * 
	 * @param userId 用户编号
	 * @return 菜单权限信息
	 */
	public Set<String> getRolePermission(Long userId) {
		Set<String> roles = new HashSet<String>();
		// 管理员拥有所有权限
		if (1 == userId) {
			roles.add("admin");
		} else {
			roles.addAll(roleService.selectRolePermissionByUserId(userId));
		}
		return roles;
	}
}