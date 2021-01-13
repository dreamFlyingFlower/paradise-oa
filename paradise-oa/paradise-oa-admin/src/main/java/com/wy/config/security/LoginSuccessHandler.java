package com.wy.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wy.result.Result;

/**
 * 安全登录成功的处理
 * 
 * @author ParadiseWY
 * @date 2020年4月2日 下午11:39:55
 */
@Configuration
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// 什么都不做的话，那就直接调用父类的方法
		// super.onAuthenticationSuccess(request, response, authentication);

		// 允许跨域
		response.setHeader("Access-Control-Allow-Origin", "*");
		// 允许自定义请求头token(允许head跨域)
		response.setHeader("Access-Control-Allow-Headers",
				"token, Accept, Origin, X-Requested-With, Content-Type, Last-Modified");

		// 这里可以根据实际情况，来确定是跳转到页面或者json格式。
		// 如果是返回json格式，那么我们这么写

		Result<?> result = Result.ok("登录成功", authentication.getPrincipal());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(result));

		// 如果是要跳转到某个页面的，比如我们的那个whoim的则
		// new DefaultRedirectStrategy().sendRedirect(request, response, "/whoim");
	}
}