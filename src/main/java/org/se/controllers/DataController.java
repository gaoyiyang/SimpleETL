package org.se.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.se.data.DatasourceConfig;
import org.se.utils.DBUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

@RestController()
public class DataController {
    @RequestMapping("/dsmap")
    public JSONArray dsMap(){
        JSONArray result = new JSONArray();
        ConcurrentHashMap<String, DataSource> map = DatasourceConfig.map;
        ConcurrentHashMap.KeySetView<String, DataSource> set = map.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()){
            JSONObject temp = new JSONObject();
            String id = it.next();
            DataSource ds = map.get(id);
            temp.put(id, DBUtil.parseDsInfoWithJson(ds));
            result.add(temp);
        }
        return result;
    }

}
