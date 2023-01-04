package org.cloudxue.simplespringboot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName PropertySourceController
 * @Description:
 * @Author: xuexiao
 * @Date: 2023年01月04日 14:43:53
 * @Version 1.0
 **/
@RestController
//@PropertySource("classpath:config.properties")
public class PropertySourceController {
    @Value("${test.app.common.appName}")
    private String appName;
    @Value("${test.app.common.appVersion}")
    private String appVersion;

    @GetMapping("/getAppInfo")
    public String getAppInfo() {
        return appName + " V" + appVersion;
    }
}
