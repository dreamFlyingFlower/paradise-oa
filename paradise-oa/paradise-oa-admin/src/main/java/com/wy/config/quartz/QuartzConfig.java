//package com.wy.config.quartz;
//
//import java.util.Properties;
//
//import javax.sql.DataSource;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//
///**
// * quartz配置,需要先将quartz持久化的sql文件导入到数据库中.该方式和在配置文件中配置一样
// *
// * @author ParadiseWY
// * @date 2020年4月7日 上午10:08:18
// */
//@Configuration
//public class QuartzConfig {
//
//	@Bean
//	public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
//		// quartz参数
//		Properties prop = new Properties();
//		// 调度器的实体名称
//		prop.put("org.quartz.scheduler.instanceName", "paradiseScheduler");
//		// 设置为auto时,默认的实现org.quartz.scheduler.SimpleInstanceGenerator,基于主机名和时间戳自动生成实例编号
//		prop.put("org.quartz.scheduler.instanceId", "AUTO");
//		// 线程池配置
//		prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
//		prop.put("org.quartz.threadPool.threadCount", "20");
//		prop.put("org.quartz.threadPool.threadPriority", "5");
//		// JobStore配置,Scheduler在运行时用来存储相关的信息,JDBCJobStore和JobStoreTX都使用关系数据库来存储schedule相关信息
//		// JobStoreTX在每次执行任务后都使用commit或rollback来提交更改
//		prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
//		// 集群配置,如果有多个调度器则必须配置为true
//		prop.put("org.quartz.jobStore.isClustered", "true");
//		// 集群配置,检查其他调度器是否存活的时间间隔
//		prop.put("org.quartz.jobStore.clusterCheckinInterval", "15000");
//		// 集群配置,设置一个频度(毫秒),用于实例报告给集群中的其他实例状态
//		prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
//		// 触发器触发失败后再次触发的时间间隔
//		prop.put("org.quartz.jobStore.misfireThreshold", "12000");
//		// 数据库表前缀
//		prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
//
//		SchedulerFactoryBean factory = new SchedulerFactoryBean();
//		factory.setDataSource(dataSource);
//		factory.setQuartzProperties(prop);
//		factory.setSchedulerName("paradiseScheduler");
//		// 延时启动
//		factory.setStartupDelay(30);
//		factory.setApplicationContextSchedulerContextKey("applicationContextKey");
//		// 可选,QuartzScheduler启动时更新己存在的Job,这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
//		factory.setOverwriteExistingJobs(true);
//		// 设置自动启动,默认为true
//		factory.setAutoStartup(true);
//		return factory;
//	}
//}