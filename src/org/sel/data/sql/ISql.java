package org.sel.data.sql;

public interface ISql {
	String getColumnSql(String tableName);
	String getColumnSql();
	String getPagingSql(String sql, int page, int pageSize);
}
