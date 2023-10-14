package org.cloud.xue.concurrent;

import com.google.common.util.concurrent.*;
import org.cloud.xue.common.util.Print;
import org.cloud.xue.common.util.ThreadUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName SyncByGuavaDemo
 * @Description 使用Guava实现异步回调，完成泡茶实例
 * @Author xuexiao
 * @Date 2022/8/9 9:15 上午
 * @Version 1.0
 **/
public class SyncByGuavaDemo {
    public static final int SLEEP_GAP = 500;

    //通过Callable接口，创建清洗异步执行逻辑
    static class WashJob implements Callable<Boolean> {

        @Override
        public Boolean call() {
            try {
                Print.tcfo("洗茶杯");
                Print.tcfo("洗茶壶");
                //清洗中...
                Thread.sleep(SLEEP_GAP);
                Print.tcfo("清洗完成");
            } catch (InterruptedException e) {
                Print.tcfo("清洗任务失败，流程中断...");
                return false;
            }
            Print.tcfo("清洗工作顺利结束");
            return true;
        }
    }
    //通过Callable接口，创建烧水异步执行逻辑
    static class HotWaterJob implements Callable<Boolean> {

        @Override
        public Boolean call() {
            try {
                Print.tcfo("灌上凉水");
                Print.tcfo("开始烧水");
                //烧水中...
                Thread.sleep(SLEEP_GAP);
                Print.tcfo("水烧开了");
            } catch (InterruptedException e) {
                Print.tcfo("烧水任务失败，流程中断...");
                return false;
            }
            Print.tcfo("烧水工顺利结束");
            return true;
        }
    }

    //创建泡茶的回调逻辑
    static class DrinkJob {
        boolean waterOk = false;
        boolean cupOk = false;

        public void drinkTea() {
            if (waterOk && cupOk) {
                Print.tcfo("万事俱备，开始泡茶喝");
                this.waterOk = false; // ???
            }
        }
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("泡茶喝线程");
        DrinkJob drinkJob = new DrinkJob();

        Callable<Boolean> washJob = new WashJob();
        Callable<Boolean> hJob = new HotWaterJob();

        //通过Java线程池，创建Guava线程池
        ExecutorService jPool = Executors.newFixedThreadPool(10);
        ListeningExecutorService gPool = MoreExecutors.listeningDecorator(jPool);

        //将清洗异步执行逻辑提交到Guava线程池
        ListenableFuture<Boolean> washHook = gPool.submit(washJob);
        //设置清洗任务回调钩子
        Futures.addCallback(washHook, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean r) {
                if (r) {
                    drinkJob.cupOk = true;
                    drinkJob.drinkTea();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                Print.tcfo("清洗线程异常，不能喝茶了...");
            }
        });

        //创建烧水回调钩子
        FutureCallback<Boolean> hotWaterHook = new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean r) {
                if (r) {
                    drinkJob.waterOk = true;
                    drinkJob.drinkTea();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                Print.tcfo("烧水线程异常，不能喝茶了...");
            }
        };
        //启动烧水线程：将烧水异步执行逻辑提交给Guava线程池
        ListenableFuture<Boolean> hotFuture = gPool.submit(hJob);
        //设置烧水任务回调钩子
        Futures.addCallback(hotFuture, hotWaterHook);

        Print.tcfo("干点其他事...");
        ThreadUtil.sleepSeconds(1);
        Print.tcfo("执行完成");
    }
}
