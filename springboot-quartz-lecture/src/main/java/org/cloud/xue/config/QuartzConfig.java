package org.cloud.xue.config;

import org.cloud.xue.quartz.HelloJob;
import org.cloud.xue.quartz.ScheduledJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @Description: 注册调度工厂
 * @Author: xuexiao
 * @Date: 2023年10月18日 14:31:08
 **/
@Configuration
public class QuartzConfig {
    private static final Logger logger = LoggerFactory.getLogger(QuartzConfig.class);
    private static final String QUARTZ_PROPERTIES_PATH = "/quartz.properties";
    private static final String CRON_OF_FIXED_TIME_EXECUTE_JOB = "45 * * * * ? ";
    private static final String TRIGGER_DEFAULT_GROUP_NAME = "ScheduledGroup";

    @Autowired
    private JobFactory jobFactory;
    @Autowired
    private DataSource dataSource;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException{
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        //QuartzScheduler启动时更新已经存在的Job
        factory.setOverwriteExistingJobs(true);
        //改为从application.yml文件中读取配置
        factory.setQuartzProperties(quartzProperties());
        //使用自定义数据源
        factory.setDataSource(dataSource);

        factory.setJobFactory(jobFactory);
        //Spring关闭时，等待所有已经启动的Job结束，Spring才可以关闭
        factory.setWaitForJobsToCompleteOnShutdown(true);
        factory.setStartupDelay(10);//QuartzScheduler 延时启动
        return factory;
    }

    /**
     * 从配置文件中读取Quartz配置
     * @return
     * @throws IOException
     */
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource(QUARTZ_PROPERTIES_PATH));
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    @Bean(name = "scheduler")
    public Scheduler scheduler() throws IOException {
        Scheduler scheduler = schedulerFactoryBean().getScheduler();
        //装载定时任务，在此处配置，则工程启动时会加载到库中
//        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
//                .withIdentity("HelloWorld",TRIGGER_DEFAULT_GROUP_NAME)
//                .build();
//        //构建Trigger
//        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(CRON_OF_FIXED_TIME_EXECUTE_JOB);
//        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("HelloWorld",TRIGGER_DEFAULT_GROUP_NAME).withSchedule(scheduleBuilder).build();
//
//        try {
//            if (scheduler.checkExists(jobDetail.getKey()) && scheduler.checkExists(trigger.getKey())) {
//                logger.info("{}任务已经存在，无需初始化", jobDetail.getKey());
//            }else {
//                logger.info("装配定时任务：{}.{}",jobDetail.getKey(), trigger.getKey());
//                scheduler.scheduleJob(jobDetail, trigger);
//            }
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }
        return scheduler;
    }


}
