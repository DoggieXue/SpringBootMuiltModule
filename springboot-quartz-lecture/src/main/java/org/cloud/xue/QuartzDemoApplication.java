package org.cloud.xue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description:
 * @Author: xuexiao
 * @Date: 2023年10月17日 17:28:16
 **/
@SpringBootApplication
@EnableScheduling
public class QuartzDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuartzDemoApplication.class, args);
    }
}
