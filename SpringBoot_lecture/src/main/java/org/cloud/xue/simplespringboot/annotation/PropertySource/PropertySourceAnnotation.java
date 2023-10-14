package org.cloud.xue.simplespringboot.annotation.PropertySource;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @ClassName PropertySourceAnnotation
 * @Description:
 * @Author: xuexiao
 * @Date: 2023年01月04日 10:35:17
 * @Version 1.0
 **/
@PropertySource(value = {"classpath:config.properties"})
@Component("propertySourceBean")
@Data
public class PropertySourceAnnotation {

    @Value("${test.app.common.appName}")
    public String appName;
    @Value("${test.app.common.appVersion}")
    public String appVersion;

    public String appInfo;

//    @Bean
//    public String getAppInfo() {
//        return appName + " V" + appVersion;
//    }

    public String getSimpleInfo() {
        return appInfo;
    }
}
