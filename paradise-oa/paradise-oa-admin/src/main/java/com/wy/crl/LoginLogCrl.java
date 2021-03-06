package com.wy.crl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.logger.Log;
import com.wy.logger.LogType;
import com.wy.model.LoginLog;
import com.wy.result.Result;
import com.wy.service.LoginLogService;

import io.swagger.annotations.Api;

/**
 * 用户登录日志API
 * 
 * @author 飞花梦影
 * @date 2021-01-13 16:08:59
 * @git {@link https://github.com/mygodness100}
 */
@Api(tags = "用户登录日志API")
@Secured({ "ROLE_SUPER_ADMIN", "ROLE_ADMIN" })
@RestController
@RequestMapping("loginLog")
public class LoginLogCrl extends AbstractCrl<LoginLog, Long> {

	@Autowired
	private LoginLogService loginLogService;

	@Secured("ROLE_SUPER_ADMIN")
	@Log(value = "登陆日志", logType = LogType.CLEAR)
	@GetMapping("clear")
	public Result<?> clear() {
		loginLogService.clear();
		return Result.ok();
	}
}