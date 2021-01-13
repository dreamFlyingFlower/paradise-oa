//package com.wy.util;
//
//import java.text.ParseException;
//import java.util.Date;
//
//import org.quartz.CronExpression;
//import org.quartz.CronScheduleBuilder;
//import org.quartz.CronTrigger;
//import org.quartz.Job;
//import org.quartz.JobBuilder;
//import org.quartz.JobDetail;
//import org.quartz.JobKey;
//import org.quartz.Scheduler;
//import org.quartz.SchedulerException;
//import org.quartz.Trigger;
//import org.quartz.TriggerBuilder;
//import org.quartz.TriggerKey;
//
//import com.wy.common.Constants;
//import com.wy.config.quartz.QuartzDisallowConcurrentExecution;
//import com.wy.config.quartz.QuartzJobExecution;
//import com.wy.enums.BooleanEnum;
//import com.wy.model.CronJob;
//import com.wy.result.ResultException;
//
///**
// * quartz的定时任务工具类
// *
// * @author ParadiseWY
// * @date 2020年4月7日 下午2:08:58
// */
//public class QuartzUtils {
//
//	/**
//	 * 得到quartz任务类
//	 * 
//	 * @param sysJob 执行计划
//	 * @return 具体执行任务类
//	 */
//	private static Class<? extends Job> getQuartzJobClass(CronJob cronJob) {
//		boolean isConcurrent = "0".equals(cronJob.getConcurrent());
//		return isConcurrent ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
//	}
//
//	/**
//	 * 生成触发器key
//	 * 
//	 * @param jobId 定时任务编号,数据库中生成的编号
//	 * @param jobGroup 定时任务所属组
//	 */
//	public static TriggerKey getTriggerKey(Long jobId, String jobGroup) {
//		return TriggerKey.triggerKey(Constants.SCHEDELE_NAME + jobId, jobGroup);
//	}
//
//	/**
//	 * 生成定时器key
//	 * 
//	 * @param jobId 定时任务编号,数据库中生成的编号
//	 * @param jobGroup 定时任务所属组
//	 */
//	public static JobKey getJobKey(Long jobId, String jobGroup) {
//		return JobKey.jobKey(Constants.SCHEDELE_NAME + jobId, jobGroup);
//	}
//
//	/**
//	 * 创建定时任务
//	 * 
//	 * @param scheduler 定时任务执行类
//	 * @param cronJob 保存定时任务参数的实体类
//	 */
//	public static void createJob(Scheduler scheduler, CronJob cronJob) {
//		Class<? extends Job> jobClass = getQuartzJobClass(cronJob);
//		// 构建job信息
//		Long jobId = cronJob.getJobId();
//		String jobGroup = cronJob.getJobGroup();
//		// 表达式调度构建器,若是简单的可以使用SimpleScheduleBuilder
//		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronJob.getCronExpression());
//		// 设置定时任务策略
//		cronScheduleBuilder = handleMisfirePolicy(cronJob, cronScheduleBuilder);
//		// 按新的cronExpression表达式构建一个新的trigger
//		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobId, jobGroup))
//				.withSchedule(cronScheduleBuilder).build();
//		// 放入参数，运行时的方法可以获取
//		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(jobId, jobGroup)).build();
//		// 存入执行定时任务时的参数
//		jobDetail.getJobDataMap().put(Constants.SCHEDELE_PARAM, cronJob);
//		try {
//			// 判断是否存在
//			if (scheduler.checkExists(getJobKey(jobId, jobGroup))) {
//				// 防止创建时存在数据问题,先移除,然后在执行创建操作
//				scheduler.deleteJob(getJobKey(jobId, jobGroup));
//				scheduler.scheduleJob(jobDetail, trigger);
//				// 若当前任务为暂停状态,暂停任务
//				if (cronJob.getState().intValue() == BooleanEnum.NO.ordinal()) {
//					scheduler.pauseJob(getJobKey(jobId, jobGroup));
//				}
//			}
//		} catch (SchedulerException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 设置定时任务策略
//	 */
//	public static CronScheduleBuilder handleMisfirePolicy(CronJob cronJob, CronScheduleBuilder cronScheduleBuilder) {
//		switch (cronJob.getMisfirePolicy()) {
//			case CronTrigger.MISFIRE_INSTRUCTION_SMART_POLICY:
//				return cronScheduleBuilder;
//			case CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW:
//				return cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
//			case CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING:
//				return cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
//			case CronTrigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY:
//				return cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
//			default:
//				return cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
//		}
//	}
//
//	/**
//	 * 判断cron表达式是否为正确的表达式
//	 *
//	 * @param cronExpression cron表达式
//	 * @return boolean true有效,false无效
//	 */
//	public static boolean isValidExpression(String cronExpression) {
//		return CronExpression.isValidExpression(cronExpression);
//	}
//
//	/**
//	 * 根据给定的cron表达式,返回下一次执行时间
//	 *
//	 * @param cronExpression cron表达式
//	 * @return Date 下次cron表达式执行时间
//	 */
//	public static Date getNextExecution(String cronExpression) {
//		try {
//			if (isValidExpression(cronExpression)) {
//				throw new ResultException("定时任务cron表达式无效");
//			}
//			CronExpression cron = new CronExpression(cronExpression);
//			return cron.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
//		} catch (ParseException e) {
//			throw new IllegalArgumentException(e.getMessage());
//		}
//	}
//}