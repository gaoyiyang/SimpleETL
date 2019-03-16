package org.sel.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.sel.data.jdbc.JdbcSource;
import org.sel.utils.reader.ConfigReader;
import org.sel.utils.reader.ConfigReaderFactory;
import org.sel.utils.reader.PathUtil;

public class JdbcConfig {
	public static JdbcConfig jc;
	private Map<String, JdbcSource> jsMap = new ConcurrentHashMap<String, JdbcSource>();
	private JdbcConfig(){
		
	}
	public synchronized static JdbcConfig getInstance(){
		if(jc==null)
			jc = new JdbcConfig();
		return jc;
	}
	
	public JdbcSource getJdbcSource(String name){
		return jsMap.get(name);
	}
	
	private void init(){
		File file = null;
		String p = DefaultConfig.jdbcSourcePath;
		if(p.indexOf("classpath:")!=-1){
			file = new File(PathUtil.getPath(p.replace("classpath:", "")));
		}else{
			file = new File(p);
		}
		File[] files = file.listFiles();
		for(File f : files){
			if(f.isFile()){
				ConfigReader reader = ConfigReaderFactory.getInstance(f.getAbsolutePath());
				if(reader == null)
					continue;
				String name = reader.getValue("jsource.name");
				String url = reader.getValue("jsource.url");
				String user = reader.getValue("jsource.user");
				String password = reader.getValue("jsource.password");
				JdbcSource js = new JdbcSource();
				js.setUrl(url);
				js.setUser(user);
				js.setPassword(password);
				jsMap.put(name, js);
			}
		}
	}
	
	{
		init();
	}
}
