package com.wy.manager;

import java.util.TimerTask;

import com.wy.model.LoginLog;
import com.wy.model.OperateLog;
import com.wy.properties.ConfigProperties;
import com.wy.service.LoginLogService;
import com.wy.service.OperateLogService;
import com.wy.util.IpUtils;
import com.wy.util.ServletUtils;
import com.wy.util.SpringContextUtils;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * 异步记录各种日志
 *
 * @author 飞花梦影
 * @date 2021-01-17 17:29:32
 * @git {@link https://github.com/mygodness100}
 */
public class AsyncFactory {

	/**
	 * 记录登陆信息
	 * 
	 * @param username 用户名
	 * @param status 状态
	 * @param message 消息
	 * @param args 列表
	 * @return 任务task
	 */
	public static TimerTask recordLogininfor(final String username, final int state, final String message,
			final Object... args) {
		final UserAgent userAgent = UserAgent
				.parseUserAgentString(ServletUtils.getHttpServletRequest().getHeader("User-Agent"));
		final String ip = IpUtils.getIpByRequest(ServletUtils.getHttpServletRequest());
		return new TimerTask() {

			@Override
			public void run() {
				String address = IpUtils.getAddressByIp(ip,
						SpringContextUtils.getBean(ConfigProperties.class).getCommon().isAddressOpt());
				LoginLog logininfor = new LoginLog();
				logininfor.setUsername(username);
				logininfor.setLoginIp(ip);
				logininfor.setLoginPlace(address);
				logininfor.setBrowser(userAgent.getBrowser().getName());
				logininfor.setOs(userAgent.getOperatingSystem().getName());
				logininfor.setRemark(message);
				logininfor.setState(state);
				SpringContextUtils.getBean(LoginLogService.class).insertSelective(logininfor);
			}
		};
	}

	/**
	 * 操作日志记录
	 * 
	 * @param operLog 操作日志信息
	 * @return 任务task
	 */
	public static TimerTask recordOper(final OperateLog operLog) {
		return new TimerTask() {

			@Override
			public void run() {
				// 远程查询操作地点
				operLog.setOperatePlace(IpUtils.getAddressByIp(operLog.getOperateIp(),
						SpringContextUtils.getBean(ConfigProperties.class).getCommon().isAddressOpt()));
				SpringContextUtils.getBean(OperateLogService.class).insertSelective(operLog);
			}
		};
	}
}