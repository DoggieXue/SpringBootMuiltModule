package org.cloud.xue.simplespringboot.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

/**
 * @ClassName MyCommandLineRunner
 * @Description:
 * @Author: xuexiao
 * @Date: 2022年12月20日 11:47:13
 * @Version 1.0
 **/
@Slf4j
@Service
public class MyCommandLineRunner implements CommandLineRunner {

    @Value("${params.key}")
    private String key;

    @Override
    public void run(String... args) throws Exception {
        log.info("加载CommandLineRunner，执行run方法... init key = {}", key);
    }
}
