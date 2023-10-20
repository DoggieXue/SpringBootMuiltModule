package org.cloud.xue.config;

import org.quartz.Scheduler;
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
    private static final String QUARTZ_PROPERTIES_PATH = "/quartz.properties";
    private static final String CRON_OF_FIXED_TIME_EXECUTE_JOB = "45 * * * * ? ";
    private static final String TRIGGER_DEFAULT_GROUP_NAME = "ScheduledGroup";

    @Autowired
    private JobFactory jobFactory;
//    @Autowired
//    private DataSource dataSource;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException{
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        //QuartzScheduler启动时更新已经存在的Job
        factory.setOverwriteExistingJobs(true);
        factory.setQuartzProperties(quartzProperties());
        factory.setJobFactory(jobFactory);
//        factory.setDataSource(dataSource);
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
        return scheduler;
    }
}
