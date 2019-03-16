package org.sel.utils.reader;

import java.io.File;
import java.net.URL;

/**
 * 路径操作
 * @author gaoyiyang
 *
 */
public class PathUtil {
	/**
	 * 获取classpath下的文件绝对路径
	 * @param file
	 * @return
	 */
	public static String getPath(String filePath){
		URL url = PathUtil.class.getClassLoader().getResource(filePath);
		if(url == null)
			return null;
		return url.getPath();
	}
	/**
	 * 获取文件后缀名
	 * @param file
	 * @return
	 */
	public static String getSuffix(String filePath){
		if(filePath == null)
			return null;
		String[] arr = filePath.split("\\.");
		if(arr.length < 2)
			return null;
		return arr[arr.length-1];
	}
}
