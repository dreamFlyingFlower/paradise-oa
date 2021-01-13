package com.wy.system.crl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wy.result.Result;
import com.wy.system.service.SystemService;

import io.swagger.annotations.Api;

/**
 * 通用请求处理,一些系统信息及日志
 *
 * @author 飞花梦影
 * @date 2021-01-13 15:09:17
 * @git {@link https://github.com/mygodness100}
 */
@Api(tags = "通用请求处理,包括验证码,文件上传下载等API")
@Controller
@RequestMapping("system")
public class SystemCrl {

	@Autowired
	private SystemService systemService;

	@GetMapping("getCpu")
	public Result<?> getCpu() {
		return Result.ok(systemService.getServer());
	}
}