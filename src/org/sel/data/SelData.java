package org.sel.data;

import java.io.ObjectInputStream.GetField;
import java.sql.Connection;
import java.sql.SQLException;

import org.sel.data.column.Columns;
import org.sel.data.jdbc.JdbcSource;
import org.sel.data.type.DatabaseType;

public class SelData {
	private JdbcSource js;
	private String tableName;
	private Connection conn;
	private Columns cols;

	public SelData(JdbcSource js, String tableName) {
		this.js = js;
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public synchronized Connection getConnection() {
		conn = js.getConnection();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public DatabaseType getDatabaseType() {
		return js.getType();
	}

	public Columns getCols() {
		if (cols == null) {
			cols = new Columns();
			cols.load(this);
		}
		return cols;
	}

}
