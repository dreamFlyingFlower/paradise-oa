package com.wy.intercepter;

import java.lang.reflect.Method;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wy.component.TokenService;
import com.wy.limit.LimitAccess;
import com.wy.result.Result;
import com.wy.util.ServletUtils;

/**
 * 限制访问拦截
 * 
 * @author 飞花梦影
 * @date 2021-02-16 11:38:42
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
public class LimitAccessInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		LimitAccess limitAccess = method.getAnnotation(LimitAccess.class);
		if (Objects.isNull(limitAccess)) {
			return true;
		}
		boolean login = limitAccess.login();
		if (login) {
			// 需要检验登录的处理
			tokenService.getLoginUser(request);
		}
		Object object = redisTemplate.opsForValue().get("key");
		if (Integer.parseInt(object.toString()) == 0) {
			// 第一次访问
			redisTemplate.opsForValue().set("key", 1, limitAccess.value(), limitAccess.timeUnit());
		} else if (Integer.parseInt(object.toString()) < limitAccess.count()) {
			// 加1
			redisTemplate.opsForValue().increment("key", 1l);
		} else {
			// 超出访问次数
			ServletUtils.resultOk(response, Result.error("请不要频繁刷新"));
			return false;
		}
		return true;
	}
}