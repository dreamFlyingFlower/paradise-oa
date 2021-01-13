package com.wy.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.wy.model.LoginLog;
import com.wy.model.OperateLog;
import com.wy.properties.ConfigProperties;
import com.wy.service.LoginLogService;
import com.wy.service.OperateLogService;
import com.wy.util.IpUtils;
import com.wy.util.spring.ServletUtils;
import com.wy.utils.StrUtils;

import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;

/**
 * 异步写记录类信息,如日志,登录等信息 FIXME
 * 
 * @author ParadiseWY
 * @date 2020年4月2日 下午11:49:49
 */
@Component
@Async
@Slf4j
public class AsyncTask {

	@Autowired
	private LoginLogService loginLogService;

	@Autowired
	private OperateLogService operationLogService;

	@Autowired
	private ConfigProperties config;

	/**
	 * 记录登陆信息
	 * 
	 * @param username 帐号
	 * @param state 状态,0失败,1成功
	 * @param message 登录消息
	 */
	public void recordLogininfo(String username, int state, String message) {
		String ip = IpUtils.getIpByRequest(ServletUtils.getHttpServletRequest());
		String address = IpUtils.getAddressByIp(ip, config.getCommon().isAddressOpt());
		log.info(StrUtils.formatBuilder("::", ip, address, username, state, message));
		UserAgent userAgent = UserAgent
				.parseUserAgentString(ServletUtils.getHttpServletRequest().getHeader("User-Agent"));
		LoginLog logininfor = LoginLog.builder().username(username).loginIp(ip).loginPlace(address)
				.browser(userAgent.getBrowser().getName()).os(userAgent.getOperatingSystem().getName()).remark(message)
				.state(state).build();
		loginLogService.insertSelective(logininfor);
	}

	/**
	 * 操作日志记录
	 * 
	 * @param operationLog 操作日志信息
	 */
	public void recordOperation(OperateLog operationLog) {
		operationLog.setOperatePlace(
				IpUtils.getAddressByIp(operationLog.getOperateIp(), config.getCommon().isAddressOpt()));
		operationLogService.insertSelective(operationLog);
	}
}