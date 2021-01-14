package com.wy.crl;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.annotation.Log;
import com.wy.base.AbstractCrl;
import com.wy.enums.BusinessType;
import com.wy.excel.ExcelModelUtils;
import com.wy.model.OperateLog;
import com.wy.properties.ConfigProperties;
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
public class OperateLogCrl extends AbstractCrl<OperateLog, Long> {

	@Autowired
	private OperateLogService operateLogService;

	@Autowired
	private ConfigProperties config;

	// @PreAuthorize("@ss.hasPermi('monitor:operlog:list')")

	@Log(title = "操作日志", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('monitor:operlog:export')")
	@GetMapping("/export")
	public Result<?> export(OperateLog operLog, HttpServletResponse response) {
		List<OperateLog> list = operateLogService.getEntitys(operLog).getData();
		ExcelModelUtils.getInstance().exportExcel(list, response,
				config.getFileinfo().getDownloadPath() + File.separator + "操作日志.xlsx");
		return Result.ok();
	}

	@Log(title = "操作日志", businessType = BusinessType.CLEAN)
	@PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
	@DeleteMapping("clear")
	public Result<?> clear() {
		operateLogService.clear();
		return Result.ok();
	}
}