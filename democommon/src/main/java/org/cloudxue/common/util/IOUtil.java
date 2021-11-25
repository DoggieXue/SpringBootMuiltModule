package org.cloudxue.common.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * @ClassName IOUtil
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/11/25 下午2:05
 * @Version 1.0
 **/
public class IOUtil {
    public static String getUserHomeResourcePath(String resName) {
        String path = System.getProperty("user.dir") + File.separator + resName;
        return path;
    }

    /**
     * 取的当前类路径下的resName资源的完整路径
     * url.getPath()获取到的路径被utf-8编码了
     * 需要用URLDecoder.decode(path, "UTF-8")解码
     * @param resName 需要获取完整路径的资源，必须以/开始
     * @return
     */
    public static String  getResourcePath (String resName) {
        URL url = IOUtil.class.getResource(resName);
        String path = "";
        if (null == url) {
            //获取classpath的完成路径
            url = IOUtil.class.getResource("/");
            path = url.getPath() + resName;
        }else {
            path = url.getPath();
        }
        String decodePath = null;
        try {
            decodePath = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodePath;
    }

    public static void closeQuietly(Closeable o) {
        if (null != o) {
            try {
                o.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
