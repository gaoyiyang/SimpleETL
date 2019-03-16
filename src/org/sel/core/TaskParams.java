package org.sel.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 定时任务参数
 * 
 * @author gaoyiyang
 *
 */
public class TaskParams {
	private Map<String, Object> map = new HashMap<String, Object>();

	public TaskParams() {

	}

	public TaskParams(Object... objs) {
		int len = objs.length;
		if (len % 2 == 1) {
			len = len - 1;
		}
		for (int i = 0; i < len; i += 2) {
			map.put(objs[i].toString(), objs[i + 1]);
		}
	}

	public void put(String key, Object o) {
		map.put(key, o);
	}

	public void remove(String key) {
		map.remove(key);
	}

	public void clear(String key){
		map.remove(key);
	}
	
	public int size(){
		return map.size();
	}
	
//	public Object[] getItem(int i){
//		if(i < 0)
//			i = 0;
//		Set<Entry<String, Object>> entry = map.entrySet();
//		Iterator<Entry<String, Object>> it = entry.iterator();
//		int index = 0;
//		while(it.hasNext()){
//			Entry<String, Object> e = it.next();
//			if(index++ == i){
//				return new Object[]{e.getKey(),e.getValue()};
//			}
//		}
//		return null;
//	}
	
	public <T> T getItem(String key){
		if(!map.containsKey(key))
			return null;
		return (T) map.get(key);
	}
}
