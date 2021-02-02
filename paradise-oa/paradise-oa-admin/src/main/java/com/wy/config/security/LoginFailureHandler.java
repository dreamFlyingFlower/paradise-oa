package com.wy.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.wy.exception.AuthException;
import com.wy.result.Result;
import com.wy.util.ServletUtils;

/**
 * security认证失败处理,只能处理登录失败的情况,若是抛出了异常,由 SecurityEntryPoint 处理
 * 
 * FIXME 登录失败是否进行锁定或其他操作
 * 
 * @author ParadiseWY
 * @date 2020-04-02 23:20:13
 */
@Configuration
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		int errorCode = 0;
		if (exception instanceof AuthException) {
			AuthException authException = (AuthException) exception;
			errorCode = authException.getCode();
		}
		Result<?> result = Result.builder().code(errorCode).msg(exception.getMessage()).build();
		ServletUtils.result(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), result);
	}
}