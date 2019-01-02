package org.sel.core;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.test.Task;

public class TaskScheduler {
	// 调度器
	public static Scheduler scheduler;

	public static void execute(String name, Class<? extends Job> taskClass, String time) {
		try {
			if (scheduler == null) {
				scheduler = StdSchedulerFactory.getDefaultScheduler();
			}
			// 触发器
			TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
			// 触发器名,触发器组
			triggerBuilder.withIdentity("selTrigger", "selTriggerGroup");
//			triggerBuilder.startNow();
			if (time != null && !"".equals(time)) {
				taskClass.newInstance().execute(null);
				// 触发器时间设定
				triggerBuilder
						.withSchedule(CronScheduleBuilder.cronSchedule(time).withMisfireHandlingInstructionDoNothing());
				// 创建Trigger对象
				CronTrigger trigger = (CronTrigger) triggerBuilder.build();
				scheduler.scheduleJob(JobBuilder.newJob(taskClass).withIdentity(name, "selJobGroup").build(), trigger);
			} else {
				JobDetail job = JobBuilder.newJob(taskClass).withIdentity(name, "selJobGroup").build();
				scheduler.scheduleJob(job, triggerBuilder.build());
			}
			if (!scheduler.isStarted())
				scheduler.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
