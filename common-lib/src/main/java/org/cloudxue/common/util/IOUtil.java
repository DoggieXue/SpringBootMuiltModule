package org.cloudxue.common.util;

import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.MappedByteBuffer;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

/**
 * @ClassName IOUtil
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/11/25 下午2:05
 * @Version 1.0
 **/
public class IOUtil {
    /**
     * 获取文件的绝对路径
     * @param resName
     * @return
     */
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

    private static DecimalFormat fileSizeFormatter = FormatUtil.decimalFormat(1);
    /**
     * 格式化文件大小
     * @param length
     * @return
     */
    public static String getFormatFileSize(long length) {
        double size = ((double) length) / (1 << 30);
        if (size > 1) {
            return fileSizeFormatter.format(size) + "GB";
        }
        size = ((double) length) / (1 << 20);
        if (size >= 1) {
            return fileSizeFormatter.format(size) + "MB";
        }
        size = ((double) length) / (1 << 10);
        if (size >= 1) {
            return fileSizeFormatter.format(size) + "KB";
        }
        return length + "B";
    }

    //读取资源目录下的文件
    public static String loadResourceFile(String resourceName) {
        return loadJarFile(IOUtil.class.getClassLoader(), resourceName);
    }


    //读jar包根目录下的文件
    public static String loadJarFile(ClassLoader loader, String resourceName) {
        InputStream in = loader.getResourceAsStream(resourceName);
        if (null == in) {
            return null;
        }
        String out = null;
        try {
            int len = in.available();
            byte[] bytes = new byte[len];

            int readLength = in.read(bytes);
            if ((long) readLength < len) {
                throw new IOException("File length error：" + len);
            }
            out = new String(bytes, Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(in);
        }

        return out;
    }


    /**
     * 根据InputStream对应的字节数组读取InputStream长度，
     * 会将InputStream指针移动至InputStream尾
     * ，不利于后续读取，
     * readInputStream(inputStream).
     * length等同于inputStream.readAllBytes().length，
     * readAllBytes从当前指针位置读取，
     * 读取后指针留在最后的位置
     */
    public static byte[] readInputStream(InputStream inputStream) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outStream.toByteArray();
    }

    public static void unmap(MappedByteBuffer mappedByteBuffer) {
        Cleaner cl = ((DirectBuffer)mappedByteBuffer).cleaner();
        if (cl != null)
            cl.clean();
    }
}
