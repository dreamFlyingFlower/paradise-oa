package com.wy.config.quartz;

import org.quartz.JobExecutionContext;

import com.wy.model.CronJob;

/**
 * 定时任务执行,允许并发执行
 *
 * @author ParadiseWY
 * @date 2020年4月7日 下午6:43:13
 */
public class QuartzJobExecution extends AbstractQuartzJob {

	@Override
	protected void doExecute(JobExecutionContext context, CronJob cronJob)  {
		QuartzInvokeHandle.invokeMethod(cronJob);
	}
}