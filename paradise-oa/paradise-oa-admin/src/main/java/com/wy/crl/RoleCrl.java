package com.wy.crl;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.annotation.Log;
import com.wy.base.AbstractCrl;
import com.wy.enums.BusinessType;
import com.wy.excel.ExcelModelUtils;
import com.wy.model.Role;
import com.wy.result.Result;
import com.wy.service.RoleService;

import io.swagger.annotations.Api;

/**
 * 角色表API
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Api(tags = "角色表API")
@RestController
@RequestMapping("role")
public class RoleCrl extends AbstractCrl<Role, Long> {

	@Autowired
	private RoleService roleService;

	@PreAuthorize("@ss.hasPermi('system:role:list')")
	@GetMapping("/list")
	public Result<?> list(Role role) {
		// startPage(); FIXME
		List<Role> list = roleService.getEntitys(role).getData();
		return Result.page(list);
	}

	@Log(title = "角色管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('system:role:export')")
	@GetMapping("export")
	public Result<?> export(Role role, HttpServletResponse response) {
		List<Role> list = roleService.getEntitys(role).getData();
		ExcelModelUtils.getInstance().exportExcel(list, response, "角色数据.xlsx");
		return Result.ok();
	}

	/**
	 * 修改保存数据权限
	 */
	@PreAuthorize("@ss.hasPermi('system:role:edit')")
	@Log(title = "角色管理", businessType = BusinessType.UPDATE)
	@PutMapping("/dataScope")
	public Result<?> dataScope(@RequestBody Role role) {
		roleService.checkRoleAllowed(role);
		return Result.result(roleService.authDataScope(role) > 0);
	}

	/**
	 * 状态修改
	 */
	@PreAuthorize("@ss.hasPermi('system:role:edit')")
	@Log(title = "角色管理", businessType = BusinessType.UPDATE)
	@PutMapping("/changeStatus")
	public Result<?> changeStatus(@RequestBody Role role) {
		roleService.checkRoleAllowed(role);
		// role.setUpdater(SecurityUtils.getUsername());
		return Result.result(roleService.updateRoleStatus(role) > 0);
	}

	/**
	 * 获取角色选择框列表
	 */
	@PreAuthorize("@ss.hasPermi('system:role:query')")
	@GetMapping("/optionselect")
	public Result<?> optionselect() {
		return roleService.getEntitys(null);
	}

	// @ApiOperation("根据角色编号保存角色菜单以及按钮权限")
	// @PostMapping("savePermissions/{roleId}")
	// public Result<?> savePermissions(@ApiParam("角色编号") @PathVariable Integer
	// roleId,
	// @ApiParam("需要保存的数据,格式为{menus:List<menuId>,halfMenus:List<halfMenuId>,buttons:List<buttonId>")
	// @RequestBody
	// Map<String, List<Integer>> params) {
	// roleService.savePermissions(roleId, params);
	// return Result.ok();
	// }
	//
	// @ApiOperation("根据角色权限查询菜单树,以及菜单下所有按钮")
	// @PostMapping("saveMenus/{roleId}")
	// public Result<?> saveMenus(@ApiParam("角色编号") @PathVariable Integer roleId,
	// @ApiParam("需要保存的数据,菜单数据中可包含按钮数据.isCheck为1的时候保存") @RequestBody
	// List<Map<String, Object>> params) {
	// roleService.saveMenus(roleId, params);
	// return Result.ok();
	// }
}