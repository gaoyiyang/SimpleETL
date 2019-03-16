package org.se.data;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.se.utils.DBUtil;
import org.se.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 读取datasource的配置
 */
public class DatasourceConfig {

    private static Logger logger = LoggerFactory.getLogger(DatasourceConfig.class);

    private static ConcurrentHashMap<String, DataSource> map;

    static {
        init();
    }

    private static void init() {
        map = new ConcurrentHashMap<String, DataSource>();
        logger.info("加载数据源");
        File f = FileUtil.getClasspathFile("dsconfig.xml");
        if (f == null) {
            logger.info("数据源加载失败");
            return;
        }
        Document doc = null;
        try {
            doc = Jsoup.parse(f, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (doc == null) {
            logger.info("数据源加载失败");
            return;
        }
        Elements dss = doc.select("datasources datasource");
        Iterator<Element> it = dss.iterator();
        while (it.hasNext()) {
            Element e = it.next();
            String id = e.attr("id");
            String driver = e.select("driver").text();
            String url = e.select("url").text();
            String user = e.select("user").text();
            String password = e.select("password").text();
            Elements es = e.select("max-size");
            int maxSize = 0;
            if (es.size() != 0) {
                String ms = es.text();
                if(StringUtils.isNumeric(ms))
                    maxSize = Integer.parseInt(ms);
            }
            DataSource ds = null;
            if(maxSize <= 0)
                ds = DBUtil.createDataSource(driver,url,user,password);
            else
                ds = DBUtil.createDataSource(driver,url,user,password,maxSize);
            map.put(id, ds);
        }
        logger.info("数据源加载成功");
    }

    public static DataSource getDatasSourceById(String id){
        return map.get(id);
    }

}
