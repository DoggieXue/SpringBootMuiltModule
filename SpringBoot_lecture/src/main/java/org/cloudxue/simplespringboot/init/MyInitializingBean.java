package org.cloudxue.simplespringboot.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName MyInitializingBean
 * @Description: 1、JDK提供的@PostConstruct注解
 *               2、Spring提供的InitializingBean接口
 *               3、SpringBoot提供的ApplicationRunner、CommandLineRunner接口
 *               启动时的默认初始化顺序验证案例
 *
 * SpringBoot
 * @Author: xuexiao
 * @Date: 2022年12月20日 15:28:52
 * @Version 1.0
 **/
@Slf4j
@Component
@Order(3)
public class MyInitializingBean implements InitializingBean, ApplicationRunner, CommandLineRunner {

    @Value("${params.key}")
    private String key;

    @PostConstruct
    public void init() {
        log.info("1、执行JDK提供的@PostConstruct注解的方法-init()... 打印成员变量key = {}", key);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("2、执行实现了Spring提供的InitializingBean接口的方法-afterPropertiesSet()... 打印成员变量key = {}", key);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("3、执行实现了Spring Boot提供的ApplicationRunner接口的方法-run(ApplicationArguments args)... 打印成员变量key = {}", key);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("4、执行实现了Spring Boot提供的CommandLineRunner接口的方法-run(String... args)... 打印成员变量key = {}", key);
    }
}
