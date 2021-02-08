package com.wy.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 由于请求头中的信息会被跨域拒绝,需要在响应中添加响应数据防止token被拦截,该拦截必须最先执行
 * 
 * @author 飞花梦影
 * @date 2021-02-09 00:49:13
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Order(Integer.MIN_VALUE)
public class ResponseFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
			FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setHeader("Access-Control-Allow-Headers", "*");
		if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
			response.setStatus(HttpStatus.OK.value());
			return;
		}
		filterChain.doFilter(request, response);
	}
}