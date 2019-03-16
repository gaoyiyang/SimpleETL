package org.sel.data.sql;

import org.sel.data.sql.impl.MysqlSql;
import org.sel.data.sql.impl.OracleSql;
import org.sel.data.type.DatabaseType;

public class SqlFactory {

	public static ISql getInstance(DatabaseType type){
		if(type == DatabaseType.MYSQL)
			return new MysqlSql();
		if(type == DatabaseType.ORACLE)
			return new OracleSql();
		return null;
	}
	
}
