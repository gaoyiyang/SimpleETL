package org.sel.utils.reader;

import org.sel.utils.reader.impl.PropertiesConfigReader;

public class ConfigReaderFactory {

	public static ConfigReader getInstance(String filePath){
		String suffix = PathUtil.getSuffix(filePath);
		if("properties".equalsIgnoreCase(suffix))
			return new PropertiesConfigReader(filePath);
		return null;
	}
	
}
