package org.cloud.xue.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * @Description: 注册Quartz任务工厂
 * @Author: xuexiao
 * @Date: 2023年10月19日 15:24:16
 **/
@Component
public class JobFactory extends AdaptableJobFactory {
    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object jobInstance =  super.createJobInstance(bundle);
        //注入
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
