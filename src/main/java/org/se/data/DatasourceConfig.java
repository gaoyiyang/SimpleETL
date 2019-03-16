package org.se.data;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 读取datasource的配置
 */
public class DatasourceConfig {

    private static Logger logger = LoggerFactory.getLogger(DatasourceConfig.class);

    public static ConcurrentHashMap<String, DataSource> map;

    static {
        init();
    }

    public static void init() {
        map = new ConcurrentHashMap<String, DataSource>();
        logger.info("加载数据源");
        File f = FileUtil.getClasspathFile("dsconfig.xml");
        if (f == null) {
            logger.info("数据源加载失败");
            return;
        }
        SAXReader reader = new SAXReader();
        Document doc = null;
        try {
            doc = reader.read(f);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (doc == null) {
            logger.info("数据源加载失败");
            return;
        }
        List<Element> dss = doc.selectNodes("/datasources/datasource[@id]");
        Iterator<Element> it = dss.iterator();
        while (it.hasNext()){
            Element e = it.next();
            String id = e.attributeValue("id");
            String driver = e.elementText("driver");
            String url = e.elementText("url");
            String user = e.elementText("user");
            String password = e.elementText("password");
            String ms = e.elementText("max-size");
            int maxSize = 0;
            if(ms!=null&&StringUtils.isNumeric(ms)){
                maxSize = Integer.parseInt(ms);
                if(maxSize < 0)
                    maxSize = 0;
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
