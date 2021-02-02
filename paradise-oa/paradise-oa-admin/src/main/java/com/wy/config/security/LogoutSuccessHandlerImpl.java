package com.wy.config.security;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wy.component.AsyncService;
import com.wy.component.TokenService;
import com.wy.enums.TipEnum;
import com.wy.model.User;
import com.wy.result.Result;

/**
 * @apiNote 自定义退出处理类
 * @author ParadiseWY
 * @date 2020年4月2日 下午11:42:49
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private AsyncService asyncService;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		User user = tokenService.getLoginUser(request);
		if (Objects.nonNull(user)) {
			String userName = user.getUsername();
			// 删除用户缓存记录
			tokenService.delLoginUser(user.getUserId());
			// 记录用户退出日志
			asyncService.recordLoginLog(userName, TipEnum.TIP_RESPONSE_SUCCESS.getCode(), "退出成功");
		}
		Result<?> result = Result.ok("登出成功", null);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(result));
	}
}