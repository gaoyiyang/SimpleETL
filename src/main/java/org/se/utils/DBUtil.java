package org.se.utils;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

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
        ds.setMinimumIdle(maxSize/2<=0?1:maxSize/2);
        return ds;
    }

}