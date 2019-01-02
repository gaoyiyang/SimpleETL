package org.test;


import org.sel.core.SimpleEtl;
import org.sel.data.SelData;
import org.sel.data.column.Columns;
import org.sel.data.jdbc.JdbcSource;
import org.sel.init.SimpleEtlInitialization;

public class Main {
	public static void main(String[] args) {
		JdbcSource js = new JdbcSource();
		js.setUrl("jdbc:mysql://140.143.9.46:3306/test?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=TRUE");
		js.setUser("root");
		js.setPassword("960205");
		SelData from = new SelData(js, "info_user");
		SelData to = new SelData(js, "info_user_etl");
		SimpleEtl se = new SimpleEtl(from, to, new SimpleEtlInitialization() {
			@Override
			public void before() {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void after(Columns fromCols, Columns toCols) {
				fromCols.setIncrement(fromCols.getColumn("create_time"));
			}
		});
		new Thread(se).start();
//		TaskScheduler.execute("test", Task.class, "");
	}
}
