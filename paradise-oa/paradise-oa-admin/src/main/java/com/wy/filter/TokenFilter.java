package com.wy.filter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wy.component.TokenService;
import com.wy.model.User;
import com.wy.properties.ConfigProperties;
import com.wy.util.SecurityUtils;
import com.wy.utils.ListUtils;

/**
 * token过滤器,验证token有效
 *
 * @author ParadiseWY
 * @date 2020年4月8日 上午12:28:32
 */
@Component
public class TokenFilter extends OncePerRequestFilter {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private ConfigProperties config;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// 不进行token拦截
		if (!config.getFilter().isTokenEnable()) {
			chain.doFilter(request, response);
			return;
		}
		// 直接放过/和/csrf
		if (request.getRequestURI().equals("/") || request.getRequestURI().equalsIgnoreCase("/csrf")
				|| request.getRequestURI().equalsIgnoreCase("/favicon.ico")) {
			chain.doFilter(request, response);
			return;
		}
		// 放过其他web请求
		if (ListUtils.isNotBlank(config.getApi().getExcludeApis())) {
			AntPathMatcher pathMatcher = new AntPathMatcher();
			List<String> excludeApis = config.getApi().getExcludeApis();
			for (String pattern : excludeApis) {
				if (pathMatcher.match(pattern, request.getRequestURI())) {
					chain.doFilter(request, response);
					return;
				}
			}
		}
		User loginUser = tokenService.getLoginUser(request);
		if (Objects.nonNull(loginUser) && Objects.isNull(SecurityUtils.getAuthentication())) {
			tokenService.verifyToken(loginUser);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,
					null, loginUser.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		chain.doFilter(request, response);
	}
}