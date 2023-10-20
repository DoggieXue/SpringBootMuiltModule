package org.cloud.xue.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @Description: 具体定时任务
 * @Author: xuexiao
 * @Date: 2023年10月18日 16:32:40
 **/
public class ScheduledJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledJob.class);
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String jobDescription = context.getJobDetail().getDescription();
        logger.info("开始执行定时任务: {}", jobDescription);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("定时任务执行结束....");
    }
}
