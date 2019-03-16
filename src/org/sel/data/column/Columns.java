package org.sel.data.column;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sel.data.SelData;
import org.sel.data.sql.ISql;
import org.sel.data.sql.SqlFactory;

public class Columns {
	private SelData sd;
	private List<Column> list = new ArrayList<Column>();
	private Map<String, Column> map = new HashMap<String, Column>();
	private List<Column> primaryKeys = new ArrayList<Column>();
	private String tableName;
	//增量字段
	private Column increment;
	public void load(SelData sd){
		this.sd = sd;
		tableName = sd.getTableName();
		ISql is = SqlFactory.getInstance(sd.getDatabaseType());
		String columnSql = is.getColumnSql();
		Connection conn = sd.getConnection();
		try {
			PreparedStatement stat = conn.prepareStatement(columnSql);
			stat.setString(1, tableName);
			ResultSet resultSet = stat.executeQuery();
			while(resultSet.next()){
				String name = resultSet.getString("name");
				String comment = resultSet.getString("comment");
				String type = resultSet.getString("type");
				String key = resultSet.getString("key");
				boolean isPrimary = false;
				if("1".equals(key)){
					isPrimary = true;
				}
				Column column = new Column(name,comment,type,isPrimary);
				list.add(column);
				map.put(name.toLowerCase(), column);
				if(isPrimary)
					primaryKeys.add(column);
			}
			resultSet.close();
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public List<Column> getPrimaryKey(){
		return primaryKeys;
	}
	public Column getColumn(String name){
		return map.get(name.toLowerCase());
	}
	public Column getColumn(int index){
		return list.get(index);
	}
	public int size(){
		return list.size();
	}
	public void setIncrement(Column increment){
		if(map.containsKey(increment.getName().toLowerCase()))
			this.increment = increment;
	}
	public Column getIncrement(){
		return increment;
	}
	public List<Column> getDeDuplication(){
		ArrayList<Column> temp = new ArrayList<Column>();
		for(Column col : list){
			if(col.isDeDuplication())
				temp.add(col);
		}
		return temp;
	}
}
