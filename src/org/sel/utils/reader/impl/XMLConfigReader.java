package org.sel.utils.reader.impl;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.sel.utils.reader.ConfigReader;

public class XMLConfigReader implements ConfigReader {

	private String path;
	private Document doc;
	private Element root;
	
	public XMLConfigReader(String path){
		this.path = path;
		SAXReader saxReader = new SAXReader();
		try {
			this.doc = saxReader.read(new File(path));
			this.root = doc.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getValue(String key) {
		if(key == null)
			return null;
		String[] keys = key.split("\\.");
		if(root==null || !keys[0].equals(root.getName())){
			return null;
		}
		Element currentElement = root;
		for(int i = 1; i < keys.length - 1; i++){
			String k = keys[i];
			currentElement = currentElement.element(k);
			if(currentElement == null)
				return null;
		}
		String k = keys[keys.length-1];
		String result = currentElement.attributeValue(k);
		if(result == null)
			result = currentElement.elementText(k);
		return result;
	}
	
	public Element getRootElement(){
		return root;
	}

	
}
