package com.wy.aspect;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

/**
 * 监听所有http请求,打印请求参数以及结果日志
 * 
 * @author ParadiseWY
 * @date 2019年4月11日 下午2:18:09
 */
@Aspect
@Component
@Slf4j
public class ControllerAspect {

	// 申明一个切点,里面是execution表达式
	@Pointcut("execution(public * com.wy.crl.*.*(..))")
	private void controllerPoint() {}

	/**
	 * 请求method前打印内容
	 * 
	 * @param joinPoint 切面
	 */
	@Before("controllerPoint()")
	public void before(JoinPoint joinPoint) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		if (!Optional.ofNullable(requestAttributes).isPresent()) {
			return;
		}
		HttpServletRequest request = requestAttributes.getRequest();
		try {
			log.info("||===============请求内容 start===============||");
			log.info("请求地址:" + request.getRequestURL().toString());
			log.info("请求方式:" + request.getMethod());
			log.info("请求方法:" + joinPoint.getSignature());
			log.info("请求参数:" + Arrays.toString(joinPoint.getArgs()));
			log.info("||===============请求内容 end=================||");
		} catch (Exception e) {
			log.error("###ControllerAspect.class before() ERROR:", e);
		}
	}

	/**
	 * 在方法执行完结后打印返回内容
	 * 
	 * @param o 结果对象
	 */
	@AfterReturning(returning = "o", pointcut = "controllerPoint()")
	public void afterReturing(Object o) {
		try {
			log.info("||--------------结果集 start----------------||");
			log.info("Response内容:" + JSON.toJSONString(o));
			log.info("||--------------结果集 end------------------||");
		} catch (Exception e) {
			log.error("###ControllerLogAspect.class afterReturing() ERROR:", e);
		}
	}
}