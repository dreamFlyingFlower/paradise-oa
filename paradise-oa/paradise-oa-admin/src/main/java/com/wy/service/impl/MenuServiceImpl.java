package com.wy.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.base.AbstractService;
import com.wy.component.TokenService;
import com.wy.mapper.MenuMapper;
import com.wy.mapper.RoleMapper;
import com.wy.mapper.RoleMenuMapper;
import com.wy.model.Menu;
import com.wy.model.MenuExample;
import com.wy.model.Role;
import com.wy.model.RoleMenu;
import com.wy.model.RoleMenuExample;
import com.wy.model.User;
import com.wy.model.vo.PermissionVo;
import com.wy.properties.ConfigProperties;
import com.wy.result.ResultException;
import com.wy.service.MenuService;
import com.wy.utils.ListUtils;
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
	private RoleMapper roleMapper;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private ConfigProperties config;

	@Autowired
	private RoleMenuMapper roleMenuMapper;

	@Override
	public List<Menu> getSelfChildren(Long menuId) {
		MenuExample example = new MenuExample();
		example.or().andPidEqualTo(menuId);
		example.or().andMenuIdEqualTo(menuId);
		return menuMapper.selectByExample(example);
	}

	/**
	 * 根据用户ID查询菜单
	 * 
	 * @param userId 用户名称
	 * @return 菜单列表
	 */
	@Override
	public List<Menu> getTreeByUserId(Long userId) {
		User loginUser = tokenService.getLoginUser(userId);
		return getTreeByUser(loginUser);
	}

	/**
	 * 根据用户信息查询菜单
	 * 
	 * @param user 用户信息
	 * @return 菜单列表
	 */
	public List<Menu> getTreeByUser(User user) {
		return getTreeByRoleId(user.getRoles().get(0).getRoleId());
	}

	/**
	 * 根据角色id查询权限信息
	 * 
	 * @param roleId 角色id
	 * @return 权限信息
	 */
	public List<PermissionVo> getPermission(Long roleId) {
		return menuMapper.selectPermissions(roleId);
	}

	/**
	 * 根据角色ID查询菜单树信息
	 * 
	 * @param roleId 角色编号
	 * @return 选中菜单列表
	 */
	@Override
	public List<Menu> getTreeByRoleId(Long roleId) {
		Role role = roleMapper.selectByPrimaryKey(roleId);
		List<Menu> menus = menuMapper.selectEntitys(Menu.builder().pid(config.getCommon().getRootMenuId()).build());
		getTreeByOther(menus, MapUtils.builder("roleId", role.getRoleType() == 0 ? null : roleId).build());
		return menus;
	}

	private void getTreeByOther(List<Menu> menus, Map<String, Object> params) {
		Map<Long, List<Menu>> leaf = getLeaf(params);
		getLeaf(menus, leaf);
	}

	@Override
	public Map<Long, List<Menu>> getLeaf(Map<String, Object> params) {
		List<Menu> menus = getTreeAll(params, null);
		return handlerTreeMap(menus, null, null);
	}

	@Override
	public List<Menu> getTreeAll(Map<String, Object> params, Map<String, Field> priAndPidField) {
		return menuMapper.selectByRoleId(
				Objects.isNull(params.get("roleId")) ? null : Long.parseLong(params.get("roleId").toString()));
	}

	@Override
	public Map<Long, List<Menu>> handlerTreeMap(List<Menu> menus, Field priField, Field pidField) {
		Map<Long, List<Menu>> result = new HashMap<>(menus.size());
		for (Menu menu : menus) {
			Long menuId = menu.getMenuId();
			List<Menu> list = result.get(menuId);
			if (ListUtils.isBlank(list)) {
				list = new ArrayList<>();
				result.put(menuId, list);
			}
			for (Menu menu1 : menus) {
				// 将本级的下级实例放入集合中
				if (menu1.getPid() == menuId) {
					list.add(menu1);
				}
			}
		}
		return result;
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

	@Override
	public void assignMenu(Long roleId, List<Menu> menus) {
		// 先清空原有的菜单以及权限分配
		RoleMenuExample roleMenuExample = new RoleMenuExample();
		roleMenuExample.createCriteria().andRoleIdEqualTo(roleId);
		roleMenuMapper.deleteByExample(roleMenuExample);
		if (ListUtils.isBlank(menus)) {
			return;
		}
		// 添加新的菜单以及权限
		List<RoleMenu> roleMenus = new ArrayList<>();
		for (Menu menu : menus) {
			if (Objects.isNull(menu.getMenuState()) || StrUtils.isBlank(menu.getPermissions())) {
				continue;
			}
			roleMenus.add(RoleMenu.builder().roleId(roleId).menuId(menu.getMenuId()).permissions(menu.getPermissions())
					.menuState(menu.getMenuState()).build());
		}
		roleMenuMapper.inserts(roleMenus);
	}
}