package org.cloudxue.simplespringboot.annotation.Profile;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;

/**
 * @ClassName TestProfile
 * @Description: 激活方式1：将spring.profiles.active属性值设置为JVM系统属性，作为全局环境变量
 *               可以通过-Dspring.profiles.active=dev
 *               也可以通过硬编码的方式System.setProperty("spring.profiles.active", "dev");
 *               激活方式2：通过ConfigurableEnvironment.setActiveProfiles方法设置
 * @Author: xuexiao
 * @Date: 2023年01月03日 15:51:36
 * @Version 1.0
 **/
public class TestProfile {
    public static void main(String[] args) {
        //激活方式一：可以将spring.profiles.active属性设置为JVM系统属性，作为全局环境变量
        System.setProperty("spring.profiles.active", "dev");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //激活方式二：通过ConfigurableEnvironment.setActiveProfiles方法设置spring.profiles.active属性值
        context.getEnvironment().setActiveProfiles("prod");
//        context.getEnvironment().setActiveProfiles("dev","prod","test");
        context.register(ProfileConfigTest.class);
        context.refresh();

        String[] names = context.getBeanNamesForType(DataSource.class);
        for (String name : names) {
            System.out.println(name);
        }
    }
}
