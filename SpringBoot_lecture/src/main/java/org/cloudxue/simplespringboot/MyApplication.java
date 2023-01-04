package org.cloudxue.simplespringboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @ClassName MyApplication
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/11/18 上午11:22
 * @Version 1.0
 **/
@Slf4j
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        log.info("开始启动项目...");
        ConfigurableApplicationContext context = SpringApplication.run(MyApplication.class, args);
        Environment env = context.getEnvironment();
        String appName = env.getProperty("test.app.common.appName");
        String appVersion = env.getProperty("test.app.common.appVersion");
        log.info("获取非默认属性配置文件config.properties配置： appName = {}", appName);
        log.info("获取非默认属性配置文件config.properties配置： appVersion = {}", appVersion);
        log.info("项目启动成功！");
    }
}
