package com.wy.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.alibaba.fastjson.JSON;
import com.wy.result.Result;
import com.wy.utils.StrUtils;

/**
 * springsecurity异常处理
 * 
 * @author ParadiseWY
 * @date 2020年4月11日 下午1:48:47
 */
public class SecurityEntryPoint extends LoginUrlAuthenticationEntryPoint {

	public SecurityEntryPoint(String loginFormUrl) {
		super("/");
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		Result<?> result = Result.builder().code(500)
				.msg(StrUtils.isBlank(authException.getMessage()) ? "您还未登录,请登录!" : authException.getMessage()).build();
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(JSON.toJSONString(result));
	}
}