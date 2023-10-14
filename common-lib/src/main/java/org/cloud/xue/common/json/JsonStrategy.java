package org.cloud.xue.common.json;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JsonStrategy
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/10/20 5:51 下午
 * @Version 1.0
 **/
public interface JsonStrategy {
    <K, V> Map<K, V> toMap(String json);

    <K, V> Map<K, V> toMap(String json, Type type);

    <T> List<T> toList(String json);

    <T> List<T> toList(String json, Type type);

    String toJson(Object object);

    String toJson(Object object, String dateFormatPattern);

    <T> T fromJson(String json, Class<T> valueType);

    <K, V> Map<K, V> objectToMap(Object fromValue);

    <T> T mapToObject(Map fromMap, Class<T> toValueType);
}
