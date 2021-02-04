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

import com.wy.component.AsyncService;
import com.wy.component.TokenService;
import com.wy.enums.TipEnum;
import com.wy.model.User;
import com.wy.result.Result;
import com.wy.util.ServletUtils;

/**
 * 自定义登出处理类
 * 
 * @author 飞花梦影
 * @date 2021-02-04 22:32:24
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private AsyncService asyncService;

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
		ServletUtils.result(response, 200, Result.ok("登出成功", null));
	}
}