package com.wy.config.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.alibaba.fastjson.JSON;
import com.wy.result.Result;
import com.wy.utils.StrUtils;

/**
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
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(JSON.toJSONString(Result.builder().code(501)
				.msg(StrUtils.isBlank(authException.getMessage()) ? "您还未登录,请登录!" : authException.getMessage())
				.build()));
		out.flush();
		out.close();
	}
}