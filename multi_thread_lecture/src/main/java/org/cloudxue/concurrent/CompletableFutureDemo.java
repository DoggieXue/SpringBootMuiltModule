package org.cloudxue.concurrent;

import org.cloudxue.common.util.Print;
import org.cloudxue.common.util.ThreadUtil;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @ClassName CompletableFutureDemo
 * @Description CompletableFuture基本使用方法示例
 * @Author xuexiao
 * @Date 2022/8/11 9:33 上午
 * @Version 1.0
 **/
public class CompletableFutureDemo {

    /**
     * runAsync 创建一个无输入、无返回值的异步子任务
     * @throws Exception
     */
    @Test
    public void runAsyncDemo() throws Exception{
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            //模拟执行1秒
            ThreadUtil.sleepSeconds(1);
            Print.tcfo("run end...");
        });
        //等待异步任务执行完成，限时等待2秒
        future.get(2, TimeUnit.SECONDS);
    }

    /**
     * 创建一个无输入、又返回的异步子任务
     * @throws Exception
     */
    @Test
    public void supplyAsyncDemo() throws Exception{
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            //模拟执行1秒
            ThreadUtil.sleepSeconds(1);
            Print.tcfo("run end...");
            return System.currentTimeMillis() - start;
        });

        long time = future.get(2, TimeUnit.SECONDS);
        Print.tcfo("异步执行耗时：" + time/1000 + "秒");
    }

    /**
     * 设置异步任务回调钩子
     * @throws Exception
     */
    @Test
    public void whenCompleteDemo() throws Exception{
        //创建异步任务
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            ThreadUtil.sleepSeconds(1);
            Print.tcfo("抛出异常!");
            throw new RuntimeException("运行时发生异常!");
        });
        //设置异步任务执行完成后的回调钩子
        future.whenComplete(new BiConsumer<Void, Throwable>() {
            @Override
            public void accept(Void t, Throwable action) {
                Print.tcfo("执行完成");
            }
        });
        //设置异步任务发生异常时的回调钩子
        future.exceptionally(new Function<Throwable, Void>() {
            @Override
            public Void apply(Throwable throwable) {
                Print.tcfo("执行失败！ " + throwable.getMessage());
                return null;
            }
        });

        //获取异步任务结果
        future.get();
    }

    /**
     * 使用handle统一处理异常和结果
     */
    @Test
    public void handleDemo() throws Exception{
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
           ThreadUtil.sleepSeconds(1);
//           Print.tcfo("Run End...");
           throw new RuntimeException("模拟异常发生");
        });

        //统一处理异常和结果
        future.handle(new BiFunction<Void, Throwable, Object>() {
            @Override
            public Object apply(Void aVoid, Throwable throwable) {
                if (throwable == null) {
                    Print.tcfo("没有发生异常！");
                } else {
                    Print.tcfo("很抱歉，发证异常了!");
                }
                return null;
            }
        });

        future.get();
    }

    /**
     * 指定线程池执行异步任务
     */
    @Test
    public void threadPoolDemo() throws Exception{
        //创建混合线程池
        ThreadPoolExecutor pool = ThreadUtil.getMixedTargetThreadPool();
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            Print.tcfo("Run start...");
            long start = System.currentTimeMillis();
            ThreadUtil.sleepSeconds(1);
            Print.tcfo("Run end...");
            return System.currentTimeMillis() - start;
        }, pool);

        long time = future.get(2, TimeUnit.SECONDS);
        Print.tcfo("异步执行耗时：" + time/1000 + "秒");
    }
}
