package org.cloudxue.common.concurrent;

import org.apache.log4j.Logger;
import org.cloudxue.common.util.ThreadUtil;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName FutureTaskScheduler
 * @Description 提交异步执行任务辅助类
 *
 * @Author xuexiao
 * @Date 2022/11/18 5:06 下午
 * @Version 1.0
 **/
public class FutureTaskScheduler extends Thread{

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * 基于链接节点的无界并发双端队列，不允许出现null
     * FIFO队列
     */
    private ConcurrentLinkedDeque<ExecuteTask> executeTaskQueue = new ConcurrentLinkedDeque<>();
    /**
     * 休眠时间
     */
    private long sleepTime = 200;
    /**
     * 混合线程池，用于执行提交的任务
     */
//    private ExecutorService pool = Executors.newFixedThreadPool(10);
    /**
     * 混合任务线程池
     */
    private ThreadPoolExecutor threadPool = ThreadUtil.getMixedTargetThreadPool();

    private static FutureTaskScheduler inst = new FutureTaskScheduler();

    private FutureTaskScheduler() {
        //启动线程
        this.start();
    }

    public static void add(ExecuteTask executeTask) {
        inst.executeTaskQueue.add(executeTask);//添加到队列头部
    }

    @Override
    public void run() {
        while (true) {
            handleTask();
            threadSleep(sleepTime);
        }
    }

    private void threadSleep(long time) {
        try {
            sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleTask() {
        try{
            ExecuteTask executeTask;
            if (executeTaskQueue.peek() != null) {//peek() 获取双端队列的头部元素，而不从队列中移除元素
                executeTask = executeTaskQueue.poll();//poll() 获取双端队列的头部元素，且从队列中移除元素
                handleTask(executeTask);
            }
        } catch (Exception e){
            logger.error(e);
        }
    }

    private void handleTask(ExecuteTask executeTask) {
//        pool.execute(new ExecuteRunnable(executeTask));
        threadPool.execute(new ExecuteRunnable(executeTask));
    }


    class ExecuteRunnable implements Runnable {

        ExecuteTask executeTask;
        ExecuteRunnable(ExecuteTask executeTask) {
            this.executeTask = executeTask;
        }

        @Override
        public void run() {
            executeTask.execute();
        }
    }
}
