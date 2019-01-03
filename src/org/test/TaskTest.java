package org.test;

import java.util.Date;

import org.sel.core.Task;
import org.sel.core.TaskParams;
import org.sel.core.TaskScheduler;

public class TaskTest extends Task{

	public static void main(String[] args) {
//		TaskScheduler.execute("test", TaskTest.class, "0/10 * * * * ?", new TaskParams("test",new Date()));
		System.out.println(new Date(1545876060000L));
	}
	@Override
	public void execute(TaskParams params) {
		Date a = params.getItem("test");
		System.out.println(a);
	}

}
