package org.test;

import org.sel.utils.reader.ConfigReader;
import org.sel.utils.reader.ConfigReaderFactory;
import org.sel.utils.reader.PathUtil;

public class TestXML {
	public static void main(String[] args) {
		String p = PathUtil.getPath("jdbc/test1.xml");
		ConfigReader reader = ConfigReaderFactory.getInstance(p);
		System.out.println(reader.getValue("jsource.url"));
	}
}
