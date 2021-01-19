package com.wy.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.base.AbstractService;
import com.wy.config.security.TokenService;
import com.wy.mapper.MenuMapper;
import com.wy.mapper.RoleMenuMapper;
import com.wy.model.Menu;
import com.wy.model.MenuExample;
import com.wy.model.Role;
import com.wy.model.RoleMenu;
import com.wy.model.User;
import com.wy.model.vo.MetaVo;
import com.wy.model.vo.RouterVo;
import com.wy.result.ResultException;
import com.wy.service.MenuService;
import com.wy.util.ServletUtils;
import com.wy.utils.MapUtils;
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
	private RoleServiceImpl roleService;

	public static final String PREMISSION_STRING = "perms[\"{0}\"]";

	@Autowired
	private RoleMenuMapper roleMenuMapper;

	@Autowired
	private TokenService tokenService;

	@Override
	public List<Menu> getSelfChildren(Long menuId) {
		MenuExample example = new MenuExample();
		example.or().andPidEqualTo(menuId);
		example.or().andMenuIdEqualTo(menuId);
		return menuMapper.selectByExample(example);
	}

	@Override
	public List<Menu> getByUserId(Long userId) {
		List<Menu> menuList = null;
		if (roleService.ifAdminUser(userId)) {
			menuList = menuMapper.selectEntitys(null);
		} else {
			menuList = menuMapper.selectByUserId(userId);
		}
		return menuList;
	}

	/**
	 * 根据用户ID查询菜单
	 * 
	 * @param userId 用户名称
	 * @return 菜单列表
	 */
	@Override
	public List<Menu> getTreeByUserId(Long userId) {
		List<Menu> menus = null;
		List<Role> roles = roleService.getByUserId(userId);
		if (roles.get(0).getRoleType() == 0) {
			menus = menuMapper.selectEntitys(null);
		} else {
			menus = menuMapper.selectByUserId(userId);
		}
		getTreeByOther(menus);
		return menus;
	}

	private void getTreeByOther(List<Menu> menus) {
		Map<Long, List<Menu>> leaf = getLeaf(null);
		getLeaf(menus, leaf);
	}

	/**
	 * 根据用户ID查询权限
	 * 
	 * @param userId 用户ID
	 * @return 权限列表
	 */
	@Override
	public Set<String> getPermsByUserId(Long userId) {
		List<String> perms = menuMapper.selectPermsByUserId(userId);
		Set<String> permsSet = new HashSet<>();
		for (String perm : perms) {
			if (StrUtils.isNotBlank(perm)) {
				permsSet.addAll(Arrays.asList(perm.trim().split(",")));
			}
		}
		return permsSet;
	}

	/**
	 * 根据角色ID查询菜单树信息
	 * 
	 * @param roleId 角色编号
	 * @return 选中菜单列表
	 */
	@Override
	public Map<String, Object> getByRoleId(Long roleId) {
		User loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
		List<Menu> menus = getByUserId(loginUser.getUserId());
		getTreeByOther(menus);
		List<RoleMenu> entitys = roleMenuMapper.selectEntitys(RoleMenu.builder().roleId(roleId).build());
		Map<String, Object> res = MapUtils.getBuilder("checkedKeys", entitys.stream().filter(t -> {
			return t.getMenuState() == 1;
		}).map(t -> {
			return t.getMenuId();
		}).collect(Collectors.toList())).put("checkedHalfKeys", entitys.stream().filter(t -> {
			return t.getMenuState() == 2;
		}).map(t -> {
			return t.getMenuId();
		}).collect(Collectors.toList())).add("menus", menus).build();
		return res;
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

	@Override
	public int delete(Long id) {
		if (menuMapper.countByEntity(Menu.builder().pid(id).build()) > 0) {
			throw new ResultException("存在子菜单,不允许删除");
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
			perms.addAll(getPermsByUserId(userId));
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
		}
		return roles;
	}
}