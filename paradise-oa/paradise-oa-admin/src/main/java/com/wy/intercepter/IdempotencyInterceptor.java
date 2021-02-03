package com.wy.intercepter;

import java.lang.reflect.Method;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wy.component.MessageService;
import com.wy.idempotent.Idempotency;
import com.wy.result.Result;
import com.wy.util.ServletUtils;

/**
 * 幂等性拦截
 * 
 * @author 飞花梦影
 * @date 2020-04-05 00:53:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
public abstract class IdempotencyInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private MessageService messageService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return super.preHandle(request, response, handler);
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		Idempotency idempotency = method.getAnnotation(Idempotency.class);
		if (Objects.isNull(idempotency)) {
			return true;
		}
		if (this.isResubmit(request)) {
			ServletUtils.resultOk(response, Result.error(messageService.getMessage("msg_request_resubmit")));
			return false;
		}
		return true;
	}

	/**
	 * 验证是否重复提交由子类实现具体的防重复提交的规则
	 * 
	 * @param httpServletRequest 请求
	 * @return 是否重复提交
	 */
	protected abstract boolean isResubmit(HttpServletRequest request);
}