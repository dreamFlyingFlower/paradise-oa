package com.wy.crl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.config.security.TokenService;
import com.wy.model.Menu;
import com.wy.model.User;
import com.wy.result.Result;
import com.wy.service.MenuService;
import com.wy.util.spring.ServletUtils;
import com.wy.utils.MapUtils;

import io.swagger.annotations.Api;

/**
 * 菜单表API
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Api(tags = "菜单表API")
@RestController
@RequestMapping("menu")
public class MenuCrl extends AbstractCrl<Menu, Long> {
	@Autowired
	private MenuService menuService;

	@Autowired
	private TokenService tokenService;

	/**
	 * 获取菜单列表
	 */
	@PreAuthorize("@ss.hasPermi('system:menu:list')")
	@GetMapping("/list")
	public Result<?> list(Menu menu) {
		User loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
		Long userId = loginUser.getUserId();
		List<Menu> menus = menuService.selectMenuList(menu, userId);
		return Result.ok(menus);
	}

	/**
	 * 获取菜单下拉树列表
	 */
	@GetMapping("/treeselect")
	public Result<?> treeselect(Menu menu) {
		User loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
		Long userId = loginUser.getUserId();
		List<Menu> menus = menuService.selectMenuList(menu, userId);
		return Result.ok(menuService.buildMenuTreeSelect(menus));
	}

	/**
	 * 加载对应角色菜单列表树
	 */
	@GetMapping(value = "/roleMenuTreeselect/{roleId}")
	public Result<?> roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
		User loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
		List<Menu> menus = menuService.selectMenuList(loginUser.getUserId());
		Map<String, Object> res = MapUtils.getBuilder("checkedKeys", menuService.selectMenuListByRoleId(roleId))
				.add("menus", menuService.buildMenuTreeSelect(menus)).build();
		return Result.ok(res);
	}

//  @ApiOperation("根据上级菜单编号获得直接下级菜单列表")
//  @GetMapping("getChildMenu/{id}")
//  public Result<?> getChildMenu(@ApiParam("上级菜单编号") @PathVariable Integer id) {
//      return Result.ok(menuService.getTree(id, true));
//  }
//
//  @ApiOperation("根据上级菜单编号获得本身以及直接下级菜单列表")
//  @GetMapping("getSelfChildren/{id}")
//  public Result<?> getSelfChildren(@ApiParam("上级菜单编号") @PathVariable Integer id) {
//      return Result.ok(menuService.getSelfChildren(id));
//  }
}