package com.wy.crl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.wy.base.AbstractCrl;
import com.wy.component.TokenService;
import com.wy.excel.ExcelModelUtils;
import com.wy.logger.Log;
import com.wy.logger.LogType;
import com.wy.model.User;
import com.wy.result.Result;
import com.wy.service.UserService;
import com.wy.util.SecurityUtils;
import com.wy.util.ServletUtils;
import com.wy.valid.ValidInserts;

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

	@Log(value = "用户管理", logType = LogType.EXPORT)
	@Secured({ "ROLE_SUPER_ADMIN", "ROLE_ADMIN" })
	@PreAuthorize("@permissionService.hasAuthority('ROLE_ADMIN:EXPORT')")
	@GetMapping("excelExport")
	public Result<?> excelExport(User user, HttpServletResponse response) {
		List<User> list = userService.getEntitys(user).getData();
		ExcelModelUtils.getInstance().exportExcel(list, response, "用户数据.xlsx");
		return Result.ok();
	}

	@Log(value = "用户管理", logType = LogType.IMPORT)
	@Secured({ "ROLE_SUPER_ADMIN", "ROLE_ADMIN" })
	@PreAuthorize("@permissionService.hasAuthority('ROLE_ADMIN:IMPORT')")
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
	@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
	@Log(value = "用户管理", logType = LogType.INSERT)
	@ApiOperation("新增用户")
	@Override
	public Result<?> create(@Validated(ValidInserts.class) @RequestBody User user, BindingResult bind) {
		if (bind.hasErrors()) {
			FieldError error = bind.getFieldError();
			assert error != null;
			return Result.error(error.getField() + error.getDefaultMessage());
		}
		user.setPassword(SecurityUtils.encode(user.getPassword()));
		return Result.result(userService.insertSelective(user));
	}

	/**
	 * 重置密码
	 * 
	 * @param user 需要重置密码的用户信息
	 * @return 成功或失败
	 */
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@Log(value = "用户管理", logType = LogType.UPDATE)
	@ApiOperation("重置密码")
	@PostMapping("resetPwd")
	public Result<?> resetPwd(@RequestBody User user) {
		return Result.result(userService.resetPwd(user) > 0);
	}

	/**
	 * 修改密码
	 */
	@Log(value = "个人信息", logType = LogType.UPDATE)
	@PreAuthorize("principal.username == #username ")
	@ApiOperation("修改密码")
	@PostMapping("updatePwd")
	public Result<?> updatePwd(String username, String oldPassword, String newPassword) {
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
	@Log(value = "用户头像", logType = LogType.UPDATE)
	@PostMapping("updateAvatar")
	public Result<?> updateAvatar(@RequestParam MultipartFile file) {
		return Result.ok(userService.updateAvatar(file));
	}
}