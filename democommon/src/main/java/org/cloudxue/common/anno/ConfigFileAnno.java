package org.cloudxue.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName ConfigFileAnno
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/10/20 5:10 下午
 * @Version 1.0
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigFileAnno {
    String file();
}
