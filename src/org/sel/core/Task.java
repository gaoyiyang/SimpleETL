package org.sel.core;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class Task implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap map = context.getJobDetail().getJobDataMap();
		TaskParams params = (TaskParams) map.get("params");
		// TODO Auto-generated method stub
		execute(params);
	}
	
	public abstract void execute(TaskParams params);

}
