package org.test;

import java.util.Date;

import org.sel.config.JdbcConfig;
import org.sel.core.SimpleEtl;
import org.sel.core.TaskParams;
import org.sel.core.TaskScheduler;
import org.sel.data.SelData;
import org.sel.data.column.Columns;
import org.sel.data.jdbc.JdbcSource;
import org.sel.init.SimpleEtlInitialization;

public class Main {
	public static void main(String[] args) {
//		//创建JDBC源
//		JdbcSource js = new JdbcSource();
//		js.setUrl(
//				"jdbc:mysql://140.143.9.46:3306/test?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=TRUE");
//		js.setUser("root");
//		js.setPassword("960205");
		
		//从配置文件中获取jdbc源
		JdbcConfig jdbcConfig = JdbcConfig.getInstance();
		JdbcSource test1 = jdbcConfig.getJdbcSource("test1");
		JdbcSource test2 = jdbcConfig.getJdbcSource("test2");
		
		//创建数据对象
		SelData from = new SelData(test1, "info_user");
		SelData to = new SelData(test2, "info_user_etl");
		//创建定时任务
		TaskScheduler.execute("test1", SimpleEtl.class, "0/5 * * * * ?",
				//传递参数
				new TaskParams("from", from, "to", to, 
						"init", 
						new SimpleEtlInitialization() {//实现初始化接口

					@Override
					public void before(Columns fromCols, Columns toCols) {//数据同步前执行
						// 设置数据来源增量同步列
						fromCols.setIncrement(fromCols.getColumn("create_time"));
					}

					@Override
					public void after() {
					}
				}));
	}
}
