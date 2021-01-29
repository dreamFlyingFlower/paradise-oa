package com.wy.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wy.component.TokenService;
import com.wy.enums.TipEnum;
import com.wy.model.User;
import com.wy.properties.ConfigProperties;
import com.wy.result.Result;
import com.wy.utils.ListUtils;

/**
 * token过滤器,验证token以及设置security为登录状态
 * 
 * @author 飞花梦影
 * @date 2020-04-08 16:19:10
 * @git {@link https://github.com/mygodness100}
 */
@Component
public class TokenFilter extends OncePerRequestFilter {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private ConfigProperties config;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// 不进行token拦截
		if (!config.getFilter().isTokenEnable()) {
			chain.doFilter(request, response);
			return;
		}
		// 可以直接放过的请求
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
		if (Objects.nonNull(loginUser)) {
			tokenService.verifyToken(loginUser);
			// 重新存入security中,让security保持登录状态
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,
					null, loginUser.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			chain.doFilter(request, response);
		} else {
			Result<?> result = Result.error(TipEnum.TIP_AUTH_TOKEN_NOT_EXIST);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
			response.getWriter().write(objectMapper.writeValueAsString(result));
		}
	}
}