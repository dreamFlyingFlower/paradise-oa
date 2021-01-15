package com.wy.manager;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 确保应用退出时能关闭后台线程
 *
 * @author ParadiseWY
 * @date 2020年4月8日 上午12:30:33
 */
@Component
@Slf4j
public class ShutdownManager {

	@PreDestroy
	public void destroy() {
		shutdownAsyncManager();
	}

	/**
	 * 停止异步执行任务
	 */
	private void shutdownAsyncManager() {
		try {
			log.info("====关闭后台任务任务线程池====");
			AsyncManager.me().shutdown();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}