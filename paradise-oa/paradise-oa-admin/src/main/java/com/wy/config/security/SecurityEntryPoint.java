package com.wy.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.wy.result.Result;
import com.wy.util.ServletUtils;
import com.wy.utils.StrUtils;

/**
 * springsecurity异常处理,不可添加到spring上下文中,否则报错
 *
 * @author 飞花梦影
 * @date 2021-02-04 22:24:36
 * @git {@link https://github.com/mygodness100}
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
		ServletUtils.result(response, 500, result);
	}
}