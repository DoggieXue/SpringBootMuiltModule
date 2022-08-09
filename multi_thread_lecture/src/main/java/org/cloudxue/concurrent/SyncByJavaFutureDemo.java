package org.cloudxue.concurrent;

import org.cloudxue.common.util.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @ClassName SyncByJavaFutureDemo
 * @Description 通过JavaFuture 【阻塞式】获取异步执行结果，实现泡茶喝流程
 * @Author xuexiao
 * @Date 2022/8/8 2:38 下午
 * @Version 1.0
 **/
public class SyncByJavaFutureDemo {
    public static final int SLEEP_GAP = 500;

    public static String getCurrThreadName() {
        return Thread.currentThread().getName();
    }

    /**
     * 清洗Job
     */
    static class WashJob implements Callable<Boolean> {

        @Override
        public Boolean call() throws Exception {
            try{
                Logger.info("洗茶壶");
                Logger.info("洗茶杯");
                //清洗中...
                Thread.sleep(SLEEP_GAP);
                Logger.info("洗完了");
            } catch (InterruptedException e) {
                Logger.info("清洗任务失败，流程中断...");
                return false;
            }
            Logger.info("清洗工作，顺利结束");
            return true;
        }
    }

    static class HotWaterJob implements Callable<Boolean> {

        @Override
        public Boolean call() throws Exception {
            try {
                Logger.info("灌上凉水");
                Logger.info("开始烧水");
                //烧水中...
                Thread.sleep(SLEEP_GAP);
                Logger.info("水烧开了");
            } catch (InterruptedException e) {
                Logger.info("烧水任务失败，流程中断...");
                return false;
            }
            Logger.info("烧水工作，顺利结束");
            return true;
        }
    }

    public static void canDrinkTea(boolean cupOk, boolean waterOk) {
        if (cupOk && waterOk) {
            Logger.info("万事俱备，泡茶喝");
        } else if (!cupOk) {
            Logger.info("清洗失败了，不能喝茶了！");
        } else if (!waterOk) {
            Logger.info("烧水失败了，不能喝茶了！");
        }
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("泡茶主线程");
        Callable<Boolean> wJob = new WashJob();
        FutureTask<Boolean> wTask = new FutureTask<>(wJob);
        Thread wThread = new Thread(wTask, "*** 烧水线程 ***");

        Callable<Boolean> hJob = new HotWaterJob();
        FutureTask<Boolean> hTask = new FutureTask(hJob);
        Thread hThread = new Thread(hTask, "^^^ 清洗线程 ^^^");

        wThread.start();
        hThread.start();

        //等待中

        try {
            boolean waterOk = hTask.get();//会阻塞主线程
            boolean cupOk = wTask.get();//会阻塞主线程
            canDrinkTea(waterOk, cupOk);
        } catch (Exception e) {
            Logger.info(getCurrThreadName() + "发生异常被中断");
        }
        Logger.info(getCurrThreadName() + "运行结束");
    }
}
