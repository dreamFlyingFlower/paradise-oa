package com.wy.config.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.wy.common.Constants;
import com.wy.enums.BooleanEnum;
import com.wy.model.CronJob;
import com.wy.model.CronJobLog;
import com.wy.service.CronJobLogService;
import com.wy.util.spring.ContextUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * quartz定时任务执行
 *
 * @author ParadiseWY
 * @date 2020年4月7日 下午4:33:38
 */
@Slf4j
public abstract class AbstractQuartzJob implements Job {

	private static final ThreadLocal<Date> EXECUTE_TIME = new ThreadLocal<>();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		CronJob cronJob = (CronJob) context.getMergedJobDataMap().get(Constants.SCHEDELE_PARAM);
		try {
			before(context, cronJob);
			if (cronJob != null) {
				doExecute(context, cronJob);
			}
			after(context, cronJob, null);
		} catch (Exception e) {
			log.error(cronJob.getJobName() + ":定时任务执行异常->", e);
			after(context, cronJob, e);
		}
	}

	/**
	 * 执行前,记录当前时间
	 * 
	 * @param context 工作执行上下文对象
	 * @param cronJob 系统计划任务
	 */
	protected void before(JobExecutionContext context, CronJob cronJob) {
		EXECUTE_TIME.set(new Date());
	}

	/**
	 * 执行后,记录执行后时间,应该设置超过多少时间就报警,或者发邮件 FIXME
	 * 
	 * @param context 工作执行上下文对象
	 * @param cronJob 系统计划任务
	 */
	protected void after(JobExecutionContext context, CronJob cronJob, Exception e) {
		Date startTime = EXECUTE_TIME.get();
		EXECUTE_TIME.remove();
		CronJobLog cronJobLog = CronJobLog.builder().jobName(cronJob.getJobName()).jobGroup(cronJob.getJobGroup())
				.invokeTarget(cronJob.getInvokeTarget()).startTime(startTime).stopTime(new Date()).build();
		long runMs = cronJobLog.getStopTime().getTime() - cronJobLog.getStartTime().getTime();
		cronJobLog.setJobMessage(cronJobLog.getJobName() + " 总共耗时:" + runMs + "毫秒");
		if (e != null) {
			cronJobLog.setState(BooleanEnum.NO.ordinal());
			String errorMsg = e.getMessage().substring(0, 2000);
			cronJobLog.setExceptionInfo(errorMsg);
		} else {
			cronJobLog.setState(BooleanEnum.YES.ordinal());
		}
		ContextUtils.getBean(CronJobLogService.class).insertSelective(cronJobLog);
	}

	/**
	 * 执行方法,由子类重载
	 * 
	 * @param context 工作执行上下文对象
	 * @param cronJob 系统计划任务
	 */
	protected abstract void doExecute(JobExecutionContext context, CronJob cronJob);
}