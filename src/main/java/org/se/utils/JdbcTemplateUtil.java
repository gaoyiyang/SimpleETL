package org.se.utils;

import org.se.data.DatasourceConfig;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 创建jdbctemplate工具
 */
public class JdbcTemplateUtil {

    public static JdbcTemplate createJdbcTemplate(DataSource ds){
        JdbcTemplate jt = new JdbcTemplate();
        jt.setDataSource(ds);
        return jt;
    }

    public static void main(String[] args) {
        JdbcTemplate jt = createJdbcTemplate(DatasourceConfig.getDatasSourceById("1"));
        List<Map<String, Object>> list = jt.queryForList("select 1");
        System.out.println(list);
    }

}
