package org.se.utils;

import java.io.File;
import java.net.URL;

/**
 * 文件工具类
 */
public class FileUtil {


    /**
     * 获取类路径下的文件
     * @param filename
     * @return
     */
    public static File getClasspathFile(String filename){
        URL path = ClassLoader.getSystemClassLoader().getResource(filename);
        File f = null;
        if(path != null)
            f = new File(path.getPath());
        if(f.exists())
            return f;
        else
            return null;
    }

}
