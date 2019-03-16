package org.sel.core;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class TaskScheduler {
	// 调度器
	public static Scheduler scheduler;

	public static void execute(String name, Class<? extends Task> taskClass, String time, TaskParams params) {
		try {
			if (scheduler == null) {
				scheduler = StdSchedulerFactory.getDefaultScheduler();
			}
			// 触发器
			TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
			// 触发器名,触发器组
			triggerBuilder.withIdentity(new Date().getTime() + "", "selTriggerGroup");
			// triggerBuilder.startNow();

			// 构造一个任务
			JobDetail first = JobBuilder.newJob(taskClass).withIdentity(name, "firstJobGroup").build();
			// 设置参数
			if (params != null)
				first.getJobDataMap().put("params", params);

			// 调用即执行一次任务
			scheduler.scheduleJob(first, triggerBuilder.build());

			// 有设置时间的情况则加入定时任务
			if (time != null && !"".equals(time)) {
				// 触发器名,触发器组
				triggerBuilder.withIdentity(new Date().getTime() + "", "selTriggerGroup");
				JobDetail job = JobBuilder.newJob(taskClass).withIdentity(name, "selJobGroup").build();
				// 设置参数
				if (params != null)
					job.getJobDataMap().put("params", params);
				// taskClass.newInstance().execute(null);
				// 触发器时间设定
				triggerBuilder
						.withSchedule(CronScheduleBuilder.cronSchedule(time).withMisfireHandlingInstructionDoNothing());
				// 创建Trigger对象
				CronTrigger trigger = (CronTrigger) triggerBuilder.build();
				// scheduler.scheduleJob(JobBuilder.newJob(taskClass).withIdentity(name,
				// "selJobGroup").build(), trigger);
				scheduler.scheduleJob(job, trigger);
			}
			if (!scheduler.isStarted())
				scheduler.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void execute(String name, Class<? extends Task> taskClass, String time) {
		execute(name, taskClass, time, null);
	}
}
