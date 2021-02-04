package com.wy.aspect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import com.alibaba.fastjson.JSON;
import com.wy.common.StatusMsg;
import com.wy.component.AsyncService;
import com.wy.component.TokenService;
import com.wy.enums.CommonEnum;
import com.wy.logger.Log;
import com.wy.model.OperateLog;
import com.wy.model.User;
import com.wy.properties.ConfigProperties;
import com.wy.util.IpUtils;
import com.wy.util.ServletUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 操作日志记录
 * 
 * @author 飞花梦影
 * @date 2021-01-13 14:21:24
 * @git {@link https://github.com/mygodness100}
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

	@Autowired
	private AsyncService asyncService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private ConfigProperties config;

	@Pointcut("@annotation(com.wy.logger.Log)")
	public void logPoint() {}

	/**
	 * 处理完请求后执行
	 * 
	 * @param joinPoint 切点
	 */
	@AfterReturning(pointcut = "logPoint()", returning = "result")
	public void afterReturning(JoinPoint joinPoint, Object result) {
		handleLog(joinPoint, null, result);
	}

	/**
	 * 拦截异常操作
	 * 
	 * @param joinPoint 切点
	 * @param e 异常
	 */
	@AfterThrowing(value = "logPoint()", throwing = "e")
	public void afterThrowing(JoinPoint joinPoint, Exception e) {
		handleLog(joinPoint, e, null);
	}

	/**
	 * 处理操作日志信息
	 * 
	 * @param joinPoint 切面
	 * @param e 异常
	 * @param result 结果
	 */
	private void handleLog(final JoinPoint joinPoint, final Exception e, Object result) {
		Log crlLog = getAnnotationLog(joinPoint);
		if (Objects.isNull(crlLog)) {
			return;
		}
		User loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
		// 处理操作日志
		OperateLog operateLog = buildOperateLog(joinPoint, crlLog, loginUser, e);
		// 保存数据库
		asyncService.recordOperateLog(operateLog);
		// 拿到保存的结果,将日志的返回值存入日志中,返回的结果可能比较大,存在数据库浪费空间
		log.info(Objects.nonNull(result) ? "编号{}日志结果:" + JSON.toJSONString(result) : "编号{}日志无返回值",
				operateLog.getOperateId());
	}

	/**
	 * 获得方法上的注解
	 * 
	 * @param joinPoint 切面信息
	 * @return 日志注解
	 */
	private Log getAnnotationLog(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		if (!(signature instanceof MethodSignature)) {
			return null;
		}
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		if (method != null) {
			return method.getAnnotation(Log.class);
		}
		return null;
	}

	/**
	 * 处理操作日志
	 * 
	 * @param joinPoint 切面
	 * @param log 日志
	 * @param loginUser 登录用户
	 * @param e 异常信息
	 * @return 操作日志
	 */
	private OperateLog buildOperateLog(JoinPoint joinPoint, Log log, User loginUser, Exception e) {
		OperateLog operateLog = new OperateLog();
		operateLog.setState(CommonEnum.YES.getCode());
		String ip = IpUtils.getIpByRequest(ServletUtils.getHttpServletRequest());
		operateLog.setOperateIp(ip);
		operateLog.setOperatePlace(IpUtils.getAddressByIp(ip, config.getCommon().isAddressOpt()));
		operateLog.setOperateUrl(ServletUtils.getHttpServletRequest().getRequestURI());
		if (loginUser != null) {
			operateLog.setOperateName(loginUser.getUsername());
		}
		if (Objects.nonNull(e)) {
			operateLog.setState(CommonEnum.NO.getCode());
			operateLog.setErrorMsg(e.getMessage().substring(0, 2000));
		}
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		operateLog.setMethod(className + "." + methodName + "()");
		operateLog.setRequestMethod(ServletUtils.getHttpServletRequest().getMethod());
		handlerLog(joinPoint, operateLog, log);
		return operateLog;
	}

	private void handlerLog(JoinPoint joinPoint, OperateLog operateLog, Log log) {
		operateLog.setTitle(log.value());
		Class<? extends StatusMsg> logOtherType = log.logOtherType();
		if (logOtherType == StatusMsg.class) {
			operateLog.setBusinessType(log.logType().getCode());
		} else {
			try {
				operateLog.setBusinessType(logOtherType.getConstructor().newInstance().getCode());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		Class<? extends StatusMsg> operatorOtherType = log.operatorOtherType();
		if (operatorOtherType == StatusMsg.class) {
			operateLog.setOperateType(log.operatorType().getCode());
		} else {
			try {
				operateLog.setOperateType(operatorOtherType.getConstructor().newInstance().getCode());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		if (log.isSaveRequest()) {
			handlerRequest(joinPoint, operateLog);
		}
	}

	/**
	 * 获取请求的参数,放到log中
	 * 
	 * @param joinPoint 切面信息
	 * @param operateLog 操作日志
	 */
	private void handlerRequest(JoinPoint joinPoint, OperateLog operateLog) {
		String requestMethod = operateLog.getRequestMethod();
		if (HttpMethod.PUT.name().equalsIgnoreCase(requestMethod)
				|| HttpMethod.POST.name().equalsIgnoreCase(requestMethod)) {
			String params = handlerArgs(joinPoint.getArgs());
			operateLog.setOperateParam(params.substring(0, 2000));
		} else {
			Map<?, ?> paramsMap = (Map<?, ?>) ServletUtils.getHttpServletRequest()
					.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
			operateLog.setOperateParam(JSON.toJSONString(paramsMap).substring(0, 2000));
		}
	}

	/**
	 * 参数拼装
	 * 
	 * @param params 参数列表
	 * @return 参数字符串
	 */
	private String handlerArgs(Object[] params) {
		if (ArrayUtils.isEmpty(params)) {
			return "";
		}
		return Stream.of(params).filter(t -> {
			return !(t instanceof MultipartFile || t instanceof HttpServletRequest || t instanceof HttpServletResponse);
		}).map(t -> {
			return JSON.toJSONString(t);
		}).collect(Collectors.joining(","));
	}
}