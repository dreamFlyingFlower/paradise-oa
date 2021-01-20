package com.wy.crl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.wy.annotation.Log;
import com.wy.base.AbstractCrl;
import com.wy.component.TokenService;
import com.wy.enums.BusinessType;
import com.wy.excel.ExcelModelUtils;
import com.wy.model.Menu;
import com.wy.model.User;
import com.wy.result.Result;
import com.wy.service.MenuService;
import com.wy.service.UserService;
import com.wy.util.SecurityUtils;
import com.wy.util.ServletUtils;
import com.wy.utils.MapUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用户表API
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Api(tags = "用户表API")
@RestController
@RequestMapping("user")
public class UserCrl extends AbstractCrl<User, Long> {

	@Autowired
	private UserService userService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private MenuService menuService;

	/**
	 * 获取用户信息
	 * 
	 * @return 用户信息
	 */
	@ApiOperation("获取用户信息")
	@GetMapping("getInfo")
	public Result<?> getInfo() {
		User user = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
		// 角色集合
		Set<String> roles = menuService.getRolePermission(user.getUserId());
		// 权限集合
		Set<String> permissions = menuService.getMenuPermission(user.getUserId());
		Map<String, Object> map = MapUtils.getBuilder("user", user).add("roles", roles).add("permissions", permissions)
				.build();
		return Result.ok(map);
	}

	/**
	 * 获取路由信息
	 * 
	 * @return 路由信息
	 */
	@ApiOperation("获取路由信息")
	@GetMapping("getRouters")
	public Result<?> getRouters() {
		User user = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
		List<Menu> menus = menuService.getTreeByUserId(user.getUserId());
		return Result.ok(menuService.buildMenus(menus));
	}

	// @ApiOperation("获取用户列表")
	// @PreAuthorize("@ss.hasPermi('system:user:list')")
	// @GetMapping("list")
	// public Result<?> list(User user) {
	// return userService.getEntitys(user);
	// }

	@Log(title = "用户管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('system:user:export')")
	@GetMapping("excelExport")
	public Result<?> excelExport(User user, HttpServletResponse response) {
		List<User> list = userService.getEntitys(user).getData();
		ExcelModelUtils.getInstance().exportExcel(list, response, "用户数据.xlsx");
		return Result.ok();
	}

	@Log(title = "用户管理", businessType = BusinessType.IMPORT)
	@PreAuthorize("@ss.hasPermi('system:user:import')")
	@PostMapping("excelImport")
	public Result<?> excelImport(MultipartFile file, boolean updateSupport) throws Exception {
		List<Map<String, Object>> userList = ExcelModelUtils.getInstance().readExcel(file.getInputStream());
		User loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
		String operName = loginUser.getUsername();
		String message = userService.importUser(JSON.parseArray(JSON.toJSONString(userList), User.class), updateSupport,
				operName);
		return Result.ok(message);
	}

	/**
	 * 新增用户
	 */
	@PreAuthorize("@ss.hasPermi('system:user:add')")
	@Log(title = "用户管理", businessType = BusinessType.INSERT)
	@PostMapping
	public Result<?> add(@Validated @RequestBody User user) {
		user.setPassword(SecurityUtils.encode(user.getPassword()));
		return Result.result(userService.insertSelective(user));
	}

	/**
	 * 重置密码
	 */
	@PreAuthorize("@ss.hasPermi('system:user:edit')")
	@Log(title = "用户管理", businessType = BusinessType.UPDATE)
	@PutMapping("resetPwd")
	public Result<?> resetPwd(@RequestBody User user) {
		return Result.result(userService.resetPwd(user) > 0);
	}

	// @PreAuthorize("@ss.hasPermi('system:user:edit')")
	// @Log(title = "用户管理", businessType = BusinessType.UPDATE)
	// @Log(title = "个人信息", businessType = BusinessType.UPDATE)
	// @PutMapping("changeStatus")
	// public Result<?> changeStatus(@RequestBody User user) {
	// return Result.result(userService.updateUserStatus(user) > 0);
	// }

	/**
	 * 修改密码
	 */
	@Log(title = "个人信息", businessType = BusinessType.UPDATE)
	@ApiOperation("修改密码")
	@PostMapping("updatePwd")
	public Result<?> updatePwd(String oldPassword, String newPassword) {
		User loginUser = SecurityUtils.getLoginUser();
		String password = loginUser.getPassword();
		if (!SecurityUtils.matches(oldPassword, password)) {
			return Result.error("修改密码失败,旧密码错误");
		}
		if (SecurityUtils.matches(newPassword, password)) {
			return Result.error("新密码不能与旧密码相同");
		}
		if (userService.resetUserPwd(loginUser.getUserId(), SecurityUtils.encode(newPassword)) > 0) {
			// 更新缓存用户密码
			loginUser.setPassword(SecurityUtils.encode(newPassword));
			tokenService.refreshToken(loginUser);
			return Result.ok();
		}
		return Result.error("修改密码异常,请联系管理员");
	}

	/**
	 * 头像上传
	 */
	@Log(title = "用户头像", businessType = BusinessType.UPDATE)
	@PostMapping("updateAvatar")
	public Result<?> updateAvatar(@RequestParam MultipartFile file) {
		return Result.ok(userService.updateAvatar(file));
	}
}