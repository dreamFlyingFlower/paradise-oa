package com.wy.config.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

import com.wy.model.CronJob;

/**
 * 定时任务,进制并发执行
 *
 * @author ParadiseWY
 * @date 2020年4月7日 下午6:13:02
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob {

	@Override
	protected void doExecute(JobExecutionContext context, CronJob sysJob) {
		QuartzInvokeHandle.invokeMethod(sysJob);
	}
}