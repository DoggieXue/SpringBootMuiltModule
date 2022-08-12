package org.cloudxue.concurrent;

import org.cloudxue.common.util.Print;
import org.cloudxue.common.util.ThreadUtil;

import java.util.concurrent.CompletableFuture;

/**
 * @ClassName SyncByCompletableFutureDemo
 * @Description 使用CompletableFuture实现异步回调，完成泡茶案例
 * @Author xuexiao
 * @Date 2022/8/12 11:19 上午
 * @Version 1.0
 **/
public class SyncByCompletableFutureDemo {
    private static final int SLEEP_GAP = 3;

    public static void main(String[] args) {
        //创建清洗任务1
        CompletableFuture<Boolean> washJob = CompletableFuture.supplyAsync(() -> {
            Print.tcfo("清洗茶壶...");
            Print.tcfo("清洗茶杯...");
            //模拟清洗过程
            ThreadUtil.sleepSeconds(SLEEP_GAP);
            Print.tcfo("清洗完毕！");
            return true;
        });
        //创建烧水任务2
        CompletableFuture<Boolean> hotJob = CompletableFuture.supplyAsync(() -> {
            Print.tcfo("灌上凉水...");
            Print.tcfo("开始烧水...");
            //模拟烧水过程
            ThreadUtil.sleepSeconds(SLEEP_GAP);
            Print.tcfo("烧水完毕！");
            return true;
        });
        //清洗任务、烧水任务完成后，执行泡茶任务
        CompletableFuture<String> drinkJob = washJob.thenCombine(hotJob, (hotOk, cupOk) -> {
            if (hotOk && cupOk) {
                Print.tcfo("准备充足，可以喝茶了~");
                return "喝上茶了~";
            }
            return "未能喝上茶！";

        });
        //等待任务泡茶任务的执行结果
        Print.tcfo(drinkJob.join());
    }
}
