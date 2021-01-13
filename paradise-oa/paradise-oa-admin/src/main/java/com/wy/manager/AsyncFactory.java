//package com.wy.manager;
//
//import java.util.TimerTask;
//
//import com.wy.model.Logininfo;
//import com.wy.model.OperationLog;
//import com.wy.service.LogininforService;
//import com.wy.service.OperationLogService;
//import com.wy.util.AddressUtils;
//import com.wy.util.IpUtils;
//import com.wy.util.LogUtils;
//import com.wy.util.ServletUtils;
//import com.wy.util.spring.SpringContextUtils;
//
//import eu.bitwalker.useragentutils.UserAgent;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * 异步工厂（产生任务用）
// * @author ruoyi
// */
//@Slf4j
//public class AsyncFactory {
//
//	/**
//	 * 记录登陆信息
//	 * @param username 用户名
//	 * @param status 状态
//	 * @param message 消息
//	 * @param args 列表
//	 * @return 任务task
//	 */
//	public static TimerTask recordLogininfor(final String username, final int state, final String message,
//			final Object... args) {
//		final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
//		final String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
//		return new TimerTask() {
//			@Override
//			public void run() {
//				String address = AddressUtils.getRealAddressByIP(ip);
//				StringBuilder s = new StringBuilder();
//				s.append(LogUtils.getBlock(ip));
//				s.append(address);
//				s.append(LogUtils.getBlock(username));
//				s.append(LogUtils.getBlock(state));
//				s.append(LogUtils.getBlock(message));
//				// 打印信息到日志
//				log.info(s.toString(), args);
//				// 获取客户端操作系统
//				String os = userAgent.getOperatingSystem().getName();
//				// 获取客户端浏览器
//				String browser = userAgent.getBrowser().getName();
//				// 封装对象
//				Logininfo logininfor = new Logininfo();
//				logininfor.setUsername(username);
//				logininfor.setIpaddr(ip);
//				logininfor.setLoginLocation(address);
//				logininfor.setBrowser(browser);
//				logininfor.setOs(os);
//				logininfor.setMsg(message);
//				logininfor.setState(state);
//				SpringContextUtils.getBean(LogininforService.class).insertLogininfor(logininfor);
//			}
//		};
//	}
//
//	/**
//	 * 操作日志记录
//	 * @param operLog 操作日志信息
//	 * @return 任务task
//	 */
//	public static TimerTask recordOper(final OperationLog operLog) {
//		return new TimerTask() {
//			@Override
//			public void run() {
//				// 远程查询操作地点
//				operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
//				SpringContextUtils.getBean(OperationLogService.class).insertOperlog(operLog);
//			}
//		};
//	}
//}