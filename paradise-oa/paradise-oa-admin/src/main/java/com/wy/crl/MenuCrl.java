package com.wy.crl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.Menu;
import com.wy.result.Result;
import com.wy.service.MenuService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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

	/**
	 * 根据用户编号获取菜单下拉树列表
	 */
	@ApiOperation("根据用户编号获取菜单下拉树列表")
	@PreAuthorize("principal.userId = #userId")
	@GetMapping("getTreeByUserId/{userId}")
	public Result<?> getTreeByUserId(@PathVariable Long userId) {
		return Result.ok(menuService.getTreeByUserId(userId));
	}

	/**
	 * 加载对应角色菜单列表树
	 */
	@ApiOperation("根据角色查询菜单树,不查相应权限")
//	@PreAuthorize("principal.userId = #userId")
	@GetMapping("getTreeByRoleId/{roleId}")
	public Result<?> getTreeByRoleId(@PathVariable("roleId") Long roleId) {
		return Result.ok(menuService.getTreeByRoleId(roleId));
	}
	
	@ApiOperation("根据角色查询菜单树,以及相应权限")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
	@GetMapping("getPermissionByRoleId/{roleId}")
	public Result<?> getPermissionByRoleId(@PathVariable Long roleId) {
		return Result.ok(menuService.getPermissionByRoleId(roleId));
	}

	@ApiOperation("根据上级菜单编号获得本身以及直接下级菜单列表")
	@GetMapping("getSelfChildren/{id}")
	public Result<?> getSelfChildren(@ApiParam("上级菜单编号") @PathVariable Long id) {
		return Result.ok(menuService.getSelfChildren(id));
	}
}