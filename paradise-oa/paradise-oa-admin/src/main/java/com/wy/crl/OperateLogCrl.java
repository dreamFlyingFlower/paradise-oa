package com.wy.crl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.logger.Log;
import com.wy.logger.LogType;
import com.wy.model.OperateLog;
import com.wy.result.Result;
import com.wy.service.OperateLogService;

import io.swagger.annotations.Api;

/**
 * 操作日志表API
 * 
 * @author 飞花梦影
 * @date 2021-01-13 14:16:54
 * @git {@link https://github.com/mygodness100}
 */
@Api(tags = "操作日志表API")
@RestController
@RequestMapping("operateLog")
@Secured({ "ROLE_SUPER_ADMIN", "ROLE_ADMIN" })
public class OperateLogCrl extends AbstractCrl<OperateLog, Long> {

	@Autowired
	private OperateLogService operateLogService;

	@Log(value = "操作日志", logType = LogType.CLEAR)
	@Secured({ "ROLE_SUPER_ADMIN" })
	@GetMapping("clear")
	public Result<?> clear() {
		operateLogService.clear();
		return Result.ok();
	}
}