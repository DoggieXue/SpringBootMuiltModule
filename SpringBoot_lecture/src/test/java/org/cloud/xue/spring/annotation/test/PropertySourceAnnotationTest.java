package org.cloud.xue.spring.annotation.test;

import org.cloud.xue.simplespringboot.annotation.PropertySource.PropertySourceAnnotation;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * @ClassName PropertySourceTest
 * @Description:
 * @Author: xuexiao
 * @Date: 2023年01月04日 15:08:24
 * @Version 1.0
 **/
public class PropertySourceAnnotationTest {
    /**
     * 通过注解获取配置文件中的值
     * 通过@PropertySource指定配置文件后，就可以使用@Value注解来获取配置文件中的值了
     */
    @Test
    public void testPropertySource1() {
        //创建IOC容器
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(PropertySourceAnnotation.class);
        String[] beanNames = context.getBeanDefinitionNames();
        Arrays.stream(beanNames).forEach(System.out::println);

//        System.out.println("********************************");
//
//        String appInfo = (String)context.getBean("getAppInfo");
//        System.out.println("From config.properties appInfo = " + appInfo);

        System.out.println("********************************");

        PropertySourceAnnotation bean = (PropertySourceAnnotation)context.getBean("propertySourceBean");
        System.out.println(bean.appName + "@Value" + bean.appVersion);
    }

    /**
     * 通过Environment获取配置值
     */
    @Test
    public void testPropertySource2() {
        //创建IOC容器
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(PropertySourceAnnotation.class);
        Environment env = context.getEnvironment();
        String appName = env.getProperty("test.app.common.appName");
        String appVersion = env.getProperty("test.app.common.appVersion");
        System.out.println("From config.properties appInfo = " + appName + " V" + appVersion);
    }

    /**
     * 通过xml方式获取配置值
     */
    @Test
    public void testPropertySource3() {
        //创建IOC容器
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:common.xml");
        PropertySourceAnnotation bean = (PropertySourceAnnotation)context.getBean("propertySourceBean");
        System.out.println(bean.getSimpleInfo());
    }

}
