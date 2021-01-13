package com.wy.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wy.exception.AuthException;
import com.wy.result.Result;

/**
 * @apiNote security认证失败处理,若无法正常返回自定义异常,可尝试实现AuthenticationEntryPoint接口 FIXME
 * @author ParadiseWY
 * @date 2020年4月2日 下午11:20:13
 */
@Configuration
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		int errorCode = 0;
		if (exception instanceof AuthException) {
			AuthException authException = (AuthException) exception;
			errorCode = authException.getCode();
		}
		Result<?> result = Result.builder().code(errorCode).msg(exception.getMessage()).build();
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(result));
	}
}