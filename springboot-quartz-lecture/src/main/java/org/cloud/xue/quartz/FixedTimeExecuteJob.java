package org.cloud.xue.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 固定时间执行定时任务
 * @Author: xuexiao
 * @Date: 2023年10月19日 11:24:01
 **/
public class FixedTimeExecuteJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.info("实现Job接口的定时任务开始执行--" + context.getScheduler().getSchedulerInstanceId());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
