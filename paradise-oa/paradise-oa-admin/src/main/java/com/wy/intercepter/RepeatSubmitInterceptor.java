package com.wy.intercepter;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.wy.annotation.RepeatSubmit;
import com.wy.result.Result;
import com.wy.util.spring.ServletUtils;

/**
 * 防止重复提交 FIXME
 * 
 * @author ParadiseWY
 * @date 2020年4月5日 下午12:53:43
 */
@Component
public abstract class RepeatSubmitInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
			if (annotation != null) {
				if (this.isRepeatSubmit(request)) {
					ServletUtils.writeResult(response, JSONObject.toJSONString(Result.error("不允许重复提交，请稍后再试")));
					return false;
				}
			}
			return true;
		} else {
			return super.preHandle(request, response, handler);
		}
	}

	/**
	 * 验证是否重复提交由子类实现具体的防重复提交的规则
	 * 
	 * @param httpServletRequest
	 * @return
	 * @throws Exception
	 */
	public abstract boolean isRepeatSubmit(HttpServletRequest request);
}
