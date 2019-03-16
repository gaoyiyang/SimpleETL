package org.sel.data.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

import org.sel.config.DefaultConfig;
import org.sel.data.type.DatabaseType;

public class JdbcSource {
	private String driver;
	private String url;
	private String user;
	private String password;
	private DatabaseType type;

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public DatabaseType getType() {
		init();
		return type;
	}

	public void setType(DatabaseType type) {
		this.type = type;
	}

	public Connection getConnection() {
		init();
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void init() {
		if (driver == null && url != null) {
			if (url.toLowerCase().indexOf("jdbc:mysql:") != -1) {
				driver = DefaultConfig.mysqlDriver;
				type = DatabaseType.MYSQL;
			} else if (url.toLowerCase().indexOf("jdbc:oracle:") != -1) {
				driver = DefaultConfig.oracleDriver;
				type = DatabaseType.ORACLE;
			}
		}
	}

}
