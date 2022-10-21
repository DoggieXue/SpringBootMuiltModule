package org.cloudxue.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @ClassName JdkHttpURLConnectionHelper
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/10/20 2:43 下午
 * @Version 1.0
 **/
public class JdkHttpURLConnectionHelper {
    /**
     * 使用JDK的 java.net.HttpURLConnection发起HTTP请求
     */
    public static String jdkGet(String url) {
        //输入流
        InputStream inputStream = null;
        //HTTP连接实例
        HttpURLConnection httpConnection = null;
        StringBuilder builder = new StringBuilder();
        try {
            URL restServiceURL = new URL(url);
            //打开HttpURLConnection连接实例
            httpConnection = (HttpURLConnection) restServiceURL.openConnection();
            //设置请求头
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Accept", "application/json");
            //建立连接，发送请求
            httpConnection.connect();
            //读取响应
            if (httpConnection.getResponseCode() != 200) {
                throw new RuntimeException("HTTP GET Request Failed with Error code : "
                        + httpConnection.getResponseCode());
            }
            //读取响应内容（字节流）
            inputStream = httpConnection.getInputStream();
            byte[] b = new byte[1024];
            int length = -1;
            while ((length = inputStream.read(b)) != -1) {
                builder.append(new String(b, 0, length));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流和连接
            quietlyClose(inputStream);
            httpConnection.disconnect();
        }
        return builder.toString();
    }

    /**
     * 安静的关闭可关闭对象
     *
     * @param closeable 可关闭对象
     */
    private static void quietlyClose(java.io.Closeable closeable) {
        if (null == closeable) return;
        try {
            closeable.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}