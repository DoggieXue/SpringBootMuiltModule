package org.cloud.xue.config;

import org.cloud.xue.quartz.ScheduledJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 定义任务描述和具体的执行时间
 * @Author: xuexiao
 * @Date: 2023年10月18日 14:31:08
 **/
@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(ScheduledJob.class)
                .withIdentity("ScheduledJob")
                .withDescription("定时打印内容")
                //每次任务执行后进行存储
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger() {
        return TriggerBuilder.newTrigger()
                //绑定job
                .forJob(jobDetail())
                //间隔7s执行一次job
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(7))
                .build();
    }
}
