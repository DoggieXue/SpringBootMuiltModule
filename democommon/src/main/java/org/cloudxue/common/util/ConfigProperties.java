package org.cloudxue.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @ClassName ConfigProperties
 * @Description 请描述类的业务用途
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
        InputStreamReader ireader = null;
        try {
            String filePath = IOUtil.getResourcePath(propertiesName);
            in = new FileInputStream(filePath);

            ireader = new InputStreamReader(in);
            properties.load(ireader);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(ireader);
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
        return Integer.parseInt(readProperty(key));
    }

    public static ConfigProperties loadFromFile(Class clazz) throws IllegalAccessException{
        ConfigProperties propertiesUtil = null;

        return propertiesUtil;
    }

    public static void loadAnnotations(Class clazz) {
        ConfigProperties configProperties = null;
        try {
            configProperties = loadFromFile(clazz);
            if (null == configProperties) return;
            Field[] fields = clazz.getDeclaredFields();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
