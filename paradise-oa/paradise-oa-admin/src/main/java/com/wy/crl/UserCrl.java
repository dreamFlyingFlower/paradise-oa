package com.wy.crl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
import com.wy.config.security.TokenService;
import com.wy.enums.BusinessType;
import com.wy.excel.ExcelModelUtils;
import com.wy.model.Menu;
import com.wy.model.User;
import com.wy.properties.ConfigProperties;
import com.wy.result.Result;
import com.wy.service.MenuService;
import com.wy.service.UserService;
import com.wy.util.FileUploadUtils;
import com.wy.util.SecurityUtils;
import com.wy.util.spring.ServletUtils;
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

	@Autowired
	private ConfigProperties config;

	/**
	 * @apiNote 登录
	 * @param username 用户名
	 * @param password 密码
	 * @param code 验证码
	 * @param uuid 唯一标识
	 * @return 结果
	 */
	// @ApiOperation("登录")
	// @GetMapping("login")
	// public Result<?> login(@ApiParam("用户名") String username,
	// @ApiParam("使用AES加密后的密码") String password,
	// @ApiParam("验证码") String code, @ApiParam("唯一标识") String uuid) {
	// return Result.ok(userService.login(username, password, code, uuid));
	// }

	/**
	 * @apiNote 获取用户信息
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
	 * @apiNote 获取路由信息
	 * @return 路由信息
	 */
	@ApiOperation("获取路由信息")
	@GetMapping("getRouters")
	public Result<?> getRouters() {
		User user = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
		List<Menu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
		return Result.ok(menuService.buildMenus(menus));
	}

	/**
	 * @apiNote 获取用户列表
	 */
	@ApiOperation("获取用户列表")
	@PreAuthorize("@ss.hasPermi('system:user:list')")
	@GetMapping("list")
	public Result<?> list(User user) {
		return userService.selectUserList(user);
	}

	@Log(title = "用户管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('system:user:export')")
	@GetMapping("export")
	public Result<?> export(User user, HttpServletResponse response) {
		List<User> list = userService.selectUserList(user).getData();
		ExcelModelUtils.getInstance().exportExcel(list, response,
				config.getCommon().getDownloadPath() + File.separator + "用户数据.xlsx");
		return Result.ok();
	}

	@Log(title = "用户管理", businessType = BusinessType.IMPORT)
	@PreAuthorize("@ss.hasPermi('system:user:import')")
	@PostMapping("importData")
	public Result<?> importData(MultipartFile file, boolean updateSupport) throws Exception {
		List<Map<String, Object>> userList = ExcelModelUtils.getInstance().readExcel(file.getInputStream());
		User loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
		String operName = loginUser.getUsername();
		String message = userService.importUser(JSON.parseArray(JSON.toJSONString(userList), User.class), updateSupport,
				operName);
		return Result.ok(message);
	}

	// @GetMapping("importTemplate")
	// public Result<?> importTemplate() {
	// ExcelUtil<User> util = new ExcelUtil<User>(User.class);
	// return util.importTemplateExcel("用户数据");
	// }

	/**
	 * 新增用户
	 */
	@PreAuthorize("@ss.hasPermi('system:user:add')")
	@Log(title = "用户管理", businessType = BusinessType.INSERT)
	@PostMapping
	public Result<?> add(@Validated @RequestBody User user) {
		user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
		return Result.result(userService.insertSelective(user));
	}

	/**
	 * 重置密码
	 */
	@PreAuthorize("@ss.hasPermi('system:user:edit')")
	@Log(title = "用户管理", businessType = BusinessType.UPDATE)
	@PutMapping("/resetPwd")
	public Result<?> resetPwd(@RequestBody User user) {
		userService.checkUserAllowed(user);
		user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
		return Result.result(userService.resetPwd(user) > 0);
	}

	/**
	 * 状态修改
	 */
	@PreAuthorize("@ss.hasPermi('system:user:edit')")
	@Log(title = "用户管理", businessType = BusinessType.UPDATE)
	@PutMapping("/changeStatus")
	public Result<?> changeStatus(@RequestBody User user) {
		userService.checkUserAllowed(user);
		// user.setUpdater(SecurityUtils.getUsername());
		return Result.result(userService.updateUserStatus(user) > 0);
	}

	// @ApiOperation("上传用户图片")
	// @PostMapping("/uploadUserIcon/{userId}")
	// public Result<?> uploadUserIcon(@ApiParam("用户图片") @RequestParam(value =
	// "file") MultipartFile file,
	// @ApiParam("用户编号") @PathVariable Integer userId) {
	// return Result.result(userService.updateUserIcon(file, userId));
	// }

	/**
	 * 个人信息
	 */
	@GetMapping
	public Result<?> profile() {
		User user = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
		Map<String, Object> map = new HashMap<>();
		map.put("user", user);
		map.put("roleGroup", userService.selectUserRoleGroup(user.getUsername()));
		return Result.ok(map);
	}

	/**
	 * 修改用户
	 */
	@Log(title = "个人信息", businessType = BusinessType.UPDATE)
	@PutMapping
	public Result<?> updateProfile(@RequestBody User user) {
		if (userService.updateUserProfile(user) > 0) {
			User loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
			// 更新缓存用户信息
			// loginUser.setNickname(user.getNickname());
			loginUser.setMobile(user.getMobile());
			loginUser.setEmail(user.getEmail());
			loginUser.setSex(user.getSex());
			tokenService.setLoginUser(loginUser);
			return Result.ok();
		}
		return Result.error("修改个人信息异常，请联系管理员");
	}

	/**
	 * 重置密码
	 */
	@Log(title = "个人信息", businessType = BusinessType.UPDATE)
	@PutMapping("/updatePwd")
	public Result<?> updatePwd(String oldPassword, String newPassword) {
		User loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
		String userName = loginUser.getUsername();
		String password = loginUser.getPassword();
		if (!SecurityUtils.matchesPassword(oldPassword, password)) {
			return Result.error("修改密码失败，旧密码错误");
		}
		if (SecurityUtils.matchesPassword(newPassword, password)) {
			return Result.error("新密码不能与旧密码相同");
		}
		if (userService.resetUserPwd(userName, SecurityUtils.encryptPassword(newPassword)) > 0) {
			// 更新缓存用户密码
			loginUser.setPassword(SecurityUtils.encryptPassword(newPassword));
			tokenService.setLoginUser(loginUser);
			return Result.ok();
		}
		return Result.error("修改密码异常，请联系管理员");
	}

	/**
	 * 头像上传
	 */
	@Log(title = "用户头像", businessType = BusinessType.UPDATE)
	@PostMapping("/avatar")
	public Result<?> avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException {
		if (!file.isEmpty()) {
			User loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
			String avatar = FileUploadUtils.upload(config.getCommon().getAvatarPath(), file);
			if (userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
				HashMap<String, String> hashMap = new HashMap<>();
				hashMap.put("imgUrl", avatar);
				// 更新缓存用户头像
				loginUser.setAvatar(avatar);
				tokenService.setLoginUser(loginUser);
				return Result.ok(hashMap);
			}
		}
		return Result.error("上传图片异常，请联系管理员");
	}
}