package org.cloud.xue.demo.lock.juclock;

import org.cloud.xue.common.util.DateUtil;
import org.cloud.xue.common.util.Print;
import org.cloud.xue.common.util.ThreadUtil;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName SemaphoreTest
 * @Description 模拟10个用户在银行柜台办业务，只有2个工作窗口
 * @Author xuexiao
 * @Date 2022/7/4 3:53 下午
 * @Version 1.0
 **/
public class SemaphoreTest {

    @Test
    public void testShareLock() throws InterruptedException{
        /**
         * 排队总人数
         */
        final int USER_TOTAL = 10;
        /**
         * 可同时受理业务的窗口数量，即并发执行的线程数
         */
        final int PERMITS_TOTAL = 2;

        final CountDownLatch latch = new CountDownLatch(USER_TOTAL);
        /**
         * 创建含有2个许可的信号量
         */
        final Semaphore semaphore = new Semaphore(PERMITS_TOTAL);

        AtomicInteger index = new AtomicInteger(0);

        //可执行实例
        Runnable runnable = () -> {
            try{
                //获取信号量
                semaphore.acquire(1);
                Print.tco(DateUtil.getTime() + ", 受理中， 服务号： " + index.getAndIncrement());
                //模拟具体业务逻辑
                ThreadUtil.sleepMilliSeconds(1000);
                //释放信号量
                semaphore.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch.countDown();
        };

        //创建10个线程
        Thread[] threads = new Thread[USER_TOTAL];
        for (int i = 0; i < USER_TOTAL; i ++) {
            threads[i] = new Thread(runnable, "线程-" + i);
        }

        //启动10个线程
        for (int i = 0; i < USER_TOTAL; i++) {
            threads[i].start();
        }
        latch.await();
    }
}
