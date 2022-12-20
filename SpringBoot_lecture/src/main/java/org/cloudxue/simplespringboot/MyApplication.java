package org.cloudxue.simplespringboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        SpringApplication.run(MyApplication.class, args);
        log.info("项目启动成功！");
    }
}
