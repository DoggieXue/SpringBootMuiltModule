package org.cloudxue.simplespringboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName InitConfigTest
 * @Description: 使用@Configuration和@Bean/@PostConstruct注解实现初始化
 * @Author: xuexiao
 * @Date: 2022年12月20日 11:54:03
 * @Version 1.0
 **/
@Slf4j
@Configuration
public class InitConfigTest {

    @Value("${params.key}")
    private String key;

    @Bean
    public String testInit() {
        log.info("init key = {}", key);
        return key;
    }
}
