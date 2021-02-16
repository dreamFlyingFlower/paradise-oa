package com.wy.crl;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.PathVariable;
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
import com.wy.util.ServletUtils;
import com.wy.valid.ValidInserts;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
	@PreAuthorize("principal.userId == #userId ")
	@ApiOperation("修改密码")
	@PostMapping("updatePwd")
	public Result<?> updatePwd(Long userId, String oldPassword, String newPassword) {
		return Result.result(userService.updatePwd(userId, oldPassword, newPassword) > 0);
	}

	/**
	 * 头像上传
	 */
	@Log(value = "用户头像", logType = LogType.UPDATE)
	@PostMapping("updateAvatar")
	public Result<?> updateAvatar(@RequestParam MultipartFile file) {
		return Result.ok(userService.updateAvatar(file));
	}

	@Log(value = "用户管理", logType = LogType.EXPORT)
	@Secured({ "ROLE_SUPER_ADMIN", "ROLE_ADMIN" })
	@PreAuthorize("@permissionService.hasAuthority('ROLE_ADMIN:EXPORT')")
	@GetMapping("excelExport")
	public Result<?> excelExport(User user, HttpServletResponse response) {
		List<User> list = userService.getEntitys(user).getData();
		ExcelModelUtils.getInstance().exportExcel(list, response, "用户数据.xlsx");
		return Result.ok();
	}

	/**
	 * 用户批量导入
	 * 
	 * @param file 要导入的excel文件
	 */
	@ApiOperation("用户批量导入")
	@Log(value = "用户管理", logType = LogType.IMPORT)
	@Secured({ "ROLE_SUPER_ADMIN", "ROLE_ADMIN" })
	@PreAuthorize("@permissionService.hasAuthority('ROLE_ADMIN:IMPORT')")
	@PostMapping("excelImport")
	public Result<?> excelImport(@RequestParam MultipartFile file) {
		int row = 0;
		try {
			List<Map<String, Object>> userList = ExcelModelUtils.getInstance().readExcel(file.getInputStream());
			User loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
			row = userService.importUser(JSON.parseArray(JSON.toJSONString(userList), User.class),
					loginUser.getUsername());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Result.result(row > 0);
	}

	/**
	 * 用户请假
	 * 
	 * @param userId 用户编号
	 * @param day 请假天数
	 */
	@ApiOperation("用户请假")
	@GetMapping("leave/{userId}")
	public Result<?> leave(@ApiParam("用户编号") @PathVariable Long userId, @ApiParam("请假天数") Integer day) {
		userService.leave(userId, day);
		return Result.ok();
	}

	/**
	 * 批准或驳回请假
	 * 
	 * @param userId 批准人编号
	 * @param taskId 请假的任务编号
	 */
	@ApiOperation("批准请假")
	@GetMapping("approve/{userId}")
	public Result<?> approve(@ApiParam("用户编号") @PathVariable Long userId, @ApiParam("请假的任务编号") Long taskId) {
		userService.approve(userId, taskId);
		return Result.ok();
	}
}