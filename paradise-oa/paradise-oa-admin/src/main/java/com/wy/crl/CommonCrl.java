package com.wy.crl;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.component.RedisService;
import com.wy.enums.RequestSource;
import com.wy.io.ImageUtils;
import com.wy.properties.ConfigProperties;
import com.wy.result.ResultException;
import com.wy.utils.StrUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 通用请求处理,包括验证码,文件上传下载等API
 *
 * @author ParadiseWY
 * @date 2020年4月5日 下午1:04:00
 */
@Api(tags = "通用请求处理,包括验证码")
@RestController
@RequestMapping("common")
public class CommonCrl {

	@Autowired
	private ConfigProperties config;

	@Autowired
	private RedisService redisService;

	/**
	 * 生成验证码 FIXME
	 * 
	 * @param request 请求,其中必须包含source参数
	 * @param response 响应
	 * @return 成功后返回唯一标识,与验证码对应,登录时可用
	 */
	@ApiOperation("生成验证码")
	@GetMapping("getCaptcha")
	public void getCaptcha(@ApiParam("请求,必须包含source参数,该参数有3个值:W表示web,A表示android,I表示ios") HttpServletRequest request,
			HttpServletResponse response) {
		String source = request.getParameter("source");
		if (StrUtils.isBlank(source)) {
			throw new ResultException("请求来源类型为空");
		}
		try {
			// 生成验证码
			String captcha = ImageUtils.obtainVerifyImage(response.getOutputStream(),
					ServletRequestUtils.getIntParameter(request, "width", config.getCaptcha().getWidth()),
					ServletRequestUtils.getIntParameter(request, "height", config.getCaptcha().getHeight()),
					config.getCaptcha().getLengh());
			if (Objects.equals(source, RequestSource.WEB.getSource())) {
				// 若是web浏览器,存入session中,通过session存入到redis中
				redisService.setStr(request.getRequestedSessionId(), captcha, config.getCaptcha().getExpireTime(),
						config.getCaptcha().getExpireUnit());
				// request.getSession().setAttribute(Constants.REDIS_KEY_CAPTCHA_CODE, verifyCode);
			} else if (Objects.equals(source, RequestSource.ANDROID.getSource())) {
				// 如果是android系统,需要根据打开app时获得的imei存储 FIXME
			} else if (Objects.equals(source, RequestSource.IOS.getSource())) {
				// 如果是ios系统,需要根据什么策略来存储 FIXME
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}