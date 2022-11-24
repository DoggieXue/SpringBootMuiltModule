package org.cloudxue.common.util;

import org.cloudxue.common.anno.ConfigFileAnnotation;
import org.springframework.util.StringUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @ClassName ConfigProperties
 * @Description properties属性文件读取类
 * @Author xuexiao
 * @Date 2021/11/25 下午2:01
 * @Version 1.0
 **/
public class ConfigProperties {

    private String propertiesName = "";
    private Properties properties = new Properties();

    public ConfigProperties() {

    }

    public ConfigProperties(String fileName) {
        this.propertiesName = fileName;
    }

    protected void loadFromFile () {
        InputStream in = null;
        InputStreamReader iReader = null;
        try {
            String filePath = IOUtil.getResourcePath(propertiesName);
            in = new FileInputStream(filePath);
            //解决读取非UTF-8编码的配置文件时，出现的中文乱码问题
            iReader = new InputStreamReader(in, "UTF-8");
            properties.load(iReader);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(iReader);
        }
    }

    public String readProperty(String key) {
        String value = "";
        value = properties.getProperty(key);
        return value;
    }

    public String getValue(String key) {
        return readProperty(key);
    }

    public int getIntValue(String key) {
        if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(readProperty(key))) {
            return Integer.parseInt(readProperty(key));
        } else {
            return 0;
        }
    }

    /**
     * 获取ConfigFileAnnotation注解中配置的文件路径
     * @param clazz 添加了@ConfigFileAnnotation注解的类
     * @return
     */
    public static String getFileNameByAnnotation(Class clazz) {
        ConfigFileAnnotation annotation = (ConfigFileAnnotation) clazz.getAnnotation(ConfigFileAnnotation.class);
        return annotation.file();
    }
}
