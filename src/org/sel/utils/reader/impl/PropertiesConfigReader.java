package org.sel.utils.reader.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

import org.sel.utils.reader.ConfigReader;

public class PropertiesConfigReader implements ConfigReader{

	private String path;
	private Properties properties = new Properties();
	
	public PropertiesConfigReader(String path){
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(path),Charset.forName("UTF-8"));
			properties.load(reader);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getValue(String key) {
		return properties.getProperty(key);
	}
	
}
