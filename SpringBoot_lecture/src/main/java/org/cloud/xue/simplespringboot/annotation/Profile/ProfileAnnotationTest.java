package org.cloud.xue.simplespringboot.annotation.Profile;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @ClassName ProfileAnnotationTest
 * @Description: 硬编码方式指定@Profile注解的value值
 * @Author: xuexiao
 * @Date: 2023年01月03日 15:00:51
 * @Version 1.0
 **/
@Configuration
public class ProfileAnnotationTest {

    @Profile(value = "chinese")
    @Bean
    public Chinese getChinese() {
        return new Chinese();
    }

    @Profile("english")
    @Bean
    public English getEnglish() {
        return new English();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //硬编码方式，激活@Profile配置，实际显示指定spring.profiles.active的值
        context.getEnvironment().setActiveProfiles("chinese");
        //注册配置类
        context.register(ProfileAnnotationTest.class);
        //启动刷新容器
        context.refresh();
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefName : beanDefinitionNames) {
            System.out.println(beanDefName);
        }
    }
}

class Chinese {

}

class English {

}