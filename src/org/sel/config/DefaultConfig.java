package org.sel.config;

import org.sel.utils.reader.ConfigReader;
import org.sel.utils.reader.ConfigReaderFactory;
import org.sel.utils.reader.PathUtil;

public class DefaultConfig {
	private static final String DEFAULT_FILE = "SimpleEtl.properties";
	public static String mysqlDriver;
	public static String oracleDriver;
	public static String jdbcSourcePath;
	static {
		ConfigReader reader = ConfigReaderFactory.getInstance(PathUtil.getPath(DEFAULT_FILE));
		mysqlDriver = reader.getValue("mysql.jdbc.driver");
		oracleDriver = reader.getValue("oracle.jdbc.driver");
		jdbcSourcePath = reader.getValue("jdbc.source.path");
	}
}
