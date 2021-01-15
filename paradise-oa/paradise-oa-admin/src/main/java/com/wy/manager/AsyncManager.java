package com.wy.manager;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.wy.util.SpringContextUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 异步任务管理器
 *
 * @author ParadiseWY
 * @date 2020年4月8日 上午12:30:11
 */
@Slf4j
public class AsyncManager {

	/**
	 * 操作延迟10毫秒
	 */
	private final int OPERATE_DELAY_TIME = 10;

	/**
	 * 异步操作任务调度线程池
	 */
	private ScheduledExecutorService executor = (ScheduledExecutorService) SpringContextUtils
			.getBean("scheduledExecutorService");

	/**
	 * 单例模式
	 */
	private AsyncManager() {}

	private static AsyncManager me = new AsyncManager();

	public static AsyncManager me() {
		return me;
	}

	/**
	 * 执行任务
	 * 
	 * @param task 任务
	 */
	public void execute(TimerTask task) {
		executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
	}

	/**
	 * 停止任务线程池
	 */
	public void shutdown() {
		shutdownAndAwaitTermination(executor);
	}

	/**
	 * 停止线程池
	 * 
	 * 先使用shutdown,停止接收新任务并尝试完成所有已存在任务
	 * 如果超时,则调用shutdownNow,取消在workQueue中Pending的任务,并中断所有阻塞函数. 
	 * 如果让然超时,则強制退出.另对在shutdown时线程本身被调用中断做了处理
	 */
	public static void shutdownAndAwaitTermination(ExecutorService pool) {
		if (pool != null && !pool.isShutdown()) {
			pool.shutdown();
			try {
				if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
					pool.shutdownNow();
					if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
						log.info("Pool did not terminate");
					}
				}
			} catch (InterruptedException ie) {
				pool.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
	}
}