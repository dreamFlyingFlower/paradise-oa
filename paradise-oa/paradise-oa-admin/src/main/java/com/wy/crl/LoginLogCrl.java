package com.wy.crl;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.annotation.Log;
import com.wy.base.AbstractCrl;
import com.wy.enums.BusinessType;
import com.wy.excel.ExcelModelUtils;
import com.wy.model.LoginLog;
import com.wy.properties.ConfigProperties;
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
@RestController
@RequestMapping("loginLog")
public class LoginLogCrl extends AbstractCrl<LoginLog, Long> {

	@Autowired
	private LoginLogService loginLogService;

	@Autowired
	private ConfigProperties config;

	// @PreAuthorize("@ss.hasPermi('monitor:logininfor:list')")

	@Log(title = "登陆日志", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('monitor:logininfor:export')")
	@GetMapping("export")
	public Result<?> export(LoginLog loginLog, HttpServletResponse response) {
		List<LoginLog> list = loginLogService.selectLogininforList(loginLog).getData();
		ExcelModelUtils.getInstance().exportExcel(list, response,
				config.getCommon().getDownloadPath() + File.separator + "登陆日志.xlsx");
		return Result.ok();
	}

	@PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
	@Log(title = "登陆日志", businessType = BusinessType.CLEAN)
	@GetMapping("clear")
	public Result<?> clear() {
		loginLogService.clear();
		return Result.ok();
	}
}