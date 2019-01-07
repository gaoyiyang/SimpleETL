package org.sel.utils.reader;

import org.sel.utils.reader.impl.PropertiesConfigReader;
import org.sel.utils.reader.impl.XMLConfigReader;

public class ConfigReaderFactory {

	public static ConfigReader getInstance(String filePath){
		String suffix = PathUtil.getSuffix(filePath);
		if("properties".equalsIgnoreCase(suffix))
			return new PropertiesConfigReader(filePath);
		if("xml".equals(suffix))
			return new XMLConfigReader(filePath);
		return null;
	}
	
}
