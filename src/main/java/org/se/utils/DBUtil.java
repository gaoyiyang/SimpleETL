package org.se.utils;

import com.alibaba.fastjson.JSONObject;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.se.data.DatasourceConfig;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *  数据库工具类
 */
public class DBUtil {
    /**
     * 创建一个数据源（不使用连接池）
     * @param driverName
     * @param url
     * @param user
     * @param password
     * @return
     */
    public static DataSource createDataSource(String driverName, String url, String user, String password){
        DataSource ds = new DriverManagerDataSource();
        ((DriverManagerDataSource) ds).setDriverClassName(driverName);
        ((DriverManagerDataSource) ds).setUrl(url);
        ((DriverManagerDataSource) ds).setUsername(user);
        ((DriverManagerDataSource) ds).setPassword(password);
        return ds;
    }

    /**
     * 创建一个数据源（Hikari连接池）
     * @param driverName
     * @param url
     * @param user
     * @param password
     * @param maxSize
     * @return
     */
    public static DataSource createDataSource(String driverName, String url, String user, String password, int maxSize){
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(driverName);
        ds.setJdbcUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);
        ds.setMaximumPoolSize(maxSize);
        return ds;
    }

    /**
     * 获取queryrunner对象
     * @param ds
     * @return
     */
    public static QueryRunner createQueryRunner(DataSource ds){
        QueryRunner qr = new QueryRunner(ds);
        return qr;
    }

    /**
     * 解析数据源信息为json
     * @param ds
     * @return
     */
    public static JSONObject parseDsInfoWithJson(DataSource ds){
        JSONObject info = new JSONObject();
        if(ds == null)
            return null;
        info.put("datasource", ds.getClass());
        if(ds instanceof HikariDataSource) {
            info.put("driverName", ((HikariDataSource) ds).getDriverClassName());
            info.put("url", ((HikariDataSource) ds).getJdbcUrl());
            info.put("user", ((HikariDataSource) ds).getUsername());
            info.put("password", ((HikariDataSource) ds).getPassword());
            info.put("maximupoolsize", ((HikariDataSource) ds).getMaximumPoolSize());
        }else if(ds instanceof DriverManagerDataSource) {
            info.put("url", ((DriverManagerDataSource) ds).getUrl());
            info.put("user", ((DriverManagerDataSource) ds).getUsername());
            info.put("password", ((DriverManagerDataSource) ds).getPassword());
        }
        return info;
    }

}