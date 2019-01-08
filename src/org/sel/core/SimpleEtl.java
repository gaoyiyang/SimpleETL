package org.sel.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.sel.config.EtlConfig;
import org.sel.data.SelData;
import org.sel.data.column.Column;
import org.sel.data.column.Columns;
import org.sel.data.sql.ISql;
import org.sel.data.sql.SqlFactory;
import org.sel.init.SimpleEtlInitialization;
import org.sel.init.impl.DefaultSimpleEtlInitialization;
import org.sel.utils.reader.PathUtil;

public class SimpleEtl extends Task {
	private SelData from;
	private SelData to;
	private SimpleEtlInitialization init;
	private EtlConfig config;
	private String time = "";
	private String fromSql;
	private String insert;
	private String delete;
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	private boolean isUseParams = false;

	@Override
	public void execute(TaskParams taskParams) {
		if (!isUseParams) {
			this.from = taskParams.getItem("from");
			this.to = taskParams.getItem("to");
			SimpleEtlInitialization init = taskParams.getItem("init");
			if (init != null)
				this.init = init;
			else
				this.init = new DefaultSimpleEtlInitialization();

			EtlConfig config = taskParams.getItem("config");
			if (config != null)
				this.config = config;
			else
				this.config = new EtlConfig(true);
			isUseParams = true;
		}
		
		
		Columns fromCols = from.getCols();
		Columns toCols = to.getCols();
		init.before(from.getCols(), to.getCols());
		if (config.isAuto()) {
			config.getRc().clear();
			for (int i = 0; i < fromCols.size(); i++) {
				Column fcol = fromCols.getColumn(i);
				Column tcol = toCols.getColumn(fcol.getName());
				if (tcol == null)
					continue;
				config.add(fcol.getName(), tcol.getName());
			}
		}

		// 获取上一次时间戳后的数据
		Set<String> keys = config.getRc().keySet();
		if (fromSql == null) {
			fromSql = "select ${cols} from ${tableName}";
			StringBuffer fromSb1 = new StringBuffer();
			for (String key : keys)
				fromSb1.append(key + ",");
			fromSql = fromSql.replace("${cols}", fromSb1.substring(0, fromSb1.length() - 1)).replace("${tableName}",
					from.getTableName());
			fromSql = fromSql + " where " + from.getCols().getIncrement().getName() + ">?";
		}
		String tempSql = "select max(" + from.getCols().getIncrement().getName() + ") from " + from.getTableName();
		ISql fromIs = SqlFactory.getInstance(from.getDatabaseType());
		Connection conn = from.getConnection();
		try {
			PreparedStatement state = conn.prepareStatement(fromIs.getPagingSql(fromSql, 1, 1));
			Statement ts = conn.createStatement();
			ResultSet r = ts.executeQuery(tempSql);
			r.next();
			System.out.println(r.getString(1));
			// 获取时间戳文件
			String path = PathUtil.getPath(from.getTableName() + "_" + to.getTableName() + ".txt");
			String time = sdf.parse("1970-01-01 23:00:00.000").getTime() + "";
			if (path != null) {
				File file = new File(path);
				BufferedReader reader = new BufferedReader(new FileReader(file));
				time = reader.readLine();
			}
			state.setTimestamp(1, new Timestamp(Long.parseLong(time)));
			ResultSet result = state.executeQuery();
			boolean noZero = result.next();
			result.close();
			state.close();
//			System.out.println(noZero);
			if (!noZero) {
				conn.close();
				return;
			}
			state = conn.prepareStatement(fromIs.getPagingSql(fromSql, 1, 1000));
			state.setTimestamp(1, new Timestamp(Long.parseLong(time)));
			result = state.executeQuery();
			// 插入并去重数据
			Connection conn2 = to.getConnection();
			List<Column> pk = to.getCols().getPrimaryKey();
			List<Column> dd = to.getCols().getDeDuplication();
			if (insert == null || delete == null) {
				insert = "insert into ${tableName}(${cols}) values(${params})";
				delete = "delete from ${tableName} where (${term1}) and (${term2})";
				StringBuffer cols = new StringBuffer();
				StringBuffer params = new StringBuffer();
				StringBuffer term1 = new StringBuffer("1=1");
				StringBuffer term2 = new StringBuffer("1=1");
				for (int i = 0; i < pk.size(); i++) {
					term1.append(" and " + pk.get(i).getName() + "=?");
				}
				for (int i = 0; i < dd.size(); i++) {
					term2.append(" and " + dd.get(i).getName() + "=?");
				}
				for (String key : keys) {
					String col = config.getRc().get(key);
					cols.append(col + ",");
					params.append("?,");
				}
				insert = insert.replace("${cols}", cols.substring(0, cols.length() - 1))
						.replace("${params}", params.substring(0, params.length() - 1))
						.replace("${tableName}", to.getTableName());
				delete = delete.replace("${term1}", term1).replace("${term2}", term2).replace("${tableName}",
						to.getTableName());
			}
			PreparedStatement istate = conn2.prepareStatement(insert);
			PreparedStatement dstate = conn2.prepareStatement(delete);
			if (path == null)
				path = PathUtil.getPath("") + from.getTableName() + "_" + to.getTableName() + ".txt";
			File file = new File(path);
			if (!file.exists())
				file.createNewFile();
			while (result.next()) {
				int i = 1;
				for (Column p : pk)
					dstate.setObject(i++, result.getObject(p.getName()));
				for (Column d : dd)
					dstate.setObject(i++, result.getObject(d.getName()));
				int j = 1;
				for (String key : keys) {
					istate.setObject(j++, result.getObject(key));
				}
				time = result.getTimestamp(from.getCols().getIncrement().getName()).getTime() + "";
				System.out.println(delete);
				dstate.addBatch();
				istate.addBatch();
			}
			dstate.executeBatch();
			istate.executeBatch();
			PrintWriter out = new PrintWriter(file);
			out.println(time);
			out.flush();
			out.close();
			dstate.close();
			istate.close();
			result.close();
			state.close();
			conn2.commit();
			conn.close();
			conn2.close();
			init.after();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
