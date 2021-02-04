package com.wy.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.wy.result.Result;
import com.wy.util.ServletUtils;

/**
 * 安全登录成功的处理
 * 
 * @author 飞花梦影
 * @date 2021-02-04 22:30:05
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// 什么都不做的话,那就直接调用父类的方法
		// super.onAuthenticationSuccess(request, response, authentication);
		// 允许跨域
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 允许自定义请求头token(允许head跨域)
		response.setHeader("Access-Control-Allow-Headers",
				"token, Accept, Origin, X-Requested-With, Content-Type, Last-Modified");
		// 这里可以根据实际情况,来确定是跳转到页面或者json格式
		// 返回json格式
		Result<?> result = Result.ok("登录成功", authentication.getPrincipal());
		ServletUtils.result(response, 200, result);
		// 跳转到某个页面
		// new DefaultRedirectStrategy().sendRedirect(request, response, "/index");
	}
}