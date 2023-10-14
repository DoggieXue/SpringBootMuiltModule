package org.cloud.xue.simplespringboot.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @ClassName MyApplicationRunner
 * @Description: run方法在工程启动类的SpringApplication.run(xxx.class, args)方法之前执行
 *              可以通过@Order(1)注解指定执行顺序，数字越小，越先执行
 * @Author: xuexiao
 * @Date: 2022年12月20日 11:26:41
 * @Version 1.0
 **/
@Slf4j
@Component
public class MyApplicationRunner implements ApplicationRunner {
    @Value("${params.key}")
    private String key;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("加载ApplicationRunner，执行run方法... init key = {}", key);
    }
}
