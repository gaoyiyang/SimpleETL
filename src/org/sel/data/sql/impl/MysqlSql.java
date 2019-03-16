package org.sel.data.sql.impl;

import org.sel.data.sql.ISql;

public class MysqlSql implements ISql{

	@Override
	public String getColumnSql(String tableName) {
		
		return null;
	}

	@Override
	public String getPagingSql(String sql, int page, int pageSize) {
		String ss = "select * from (${subsql}) a limit ${pageSize} offset ${offset}";
		if(page < 1)
			page = 1;
		int offset = (page-1)*pageSize;
		return ss.replace("${subsql}", sql).replace("${pageSize}", pageSize+"").replace("${offset}", offset+"");
	}

	@Override
	public String getColumnSql() {
		return "select column_name as `name`, column_comment as `comment`, data_type as `type`, (case when column_key='PRI' then 1 else 0 end) as `key` from information_schema.columns where table_name=?";
	}

}
