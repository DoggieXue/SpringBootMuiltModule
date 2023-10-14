package org.cloud.xue.multi.thread.cas;

import org.cloud.xue.common.util.Print;
import org.cloud.xue.common.util.ThreadUtil;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @ClassName LongAdderVSAtomicLongTest
 * @Description AtomicLong、LongAdder性能比对
 * @Author xuexiao
 * @Date 2022/6/2 10:09 上午
 * @Version 1.0
 **/
public class LongAdderVSAtomicLongTest {
    //每条线程执行的轮数
    private static final int TURNS = 100000000;
    //并发任务数
    private static final int TASK_AMOUNT = 10;

    /**
     * 10个线程，每个线程循环次对AtomicLong变量累加
     */
    @Test
    public void testAtomicLong() {
        CountDownLatch latch = new CountDownLatch(TASK_AMOUNT);

        AtomicLong amount = new AtomicLong(0);

        ExecutorService pool = ThreadUtil.getCpuIntenseTargetThreadPool();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < TASK_AMOUNT; i++) {
            pool.submit(() ->{

                for (int j = 0; j < TURNS; j++) {
                    amount.incrementAndGet();
                }
                latch.countDown();
            });
        }
        //等待倒数闩完成所有的倒数任务
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        float time = (System.currentTimeMillis() - startTime) / 1000F;
        Print.tcfo("使用AtomicLong的运行时长： " + time);
        Print.tcfo("使用AtomicLong的累加结果为： " + amount.get());
    }

    /**
     * 10个线程，每个线程循环N次，对LongAdder变量累加
     */
    @Test
    public void testLongAdder() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(TASK_AMOUNT);

        LongAdder longAdder = new LongAdder();

        ExecutorService pool = ThreadUtil.getCpuIntenseTargetThreadPool();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < TASK_AMOUNT; i++) {
            pool.submit(() -> {
                for (int j = 0; j < TURNS; j++) {
                    longAdder.add(1);
                }
                latch.countDown();
            });
        }

        latch.await();

        float time = (System.currentTimeMillis() - startTime) / 1000F;
        Print.tcfo("使用LongAdder的运行时长： " + time);
        Print.tcfo("使用LongAdder的累加结果为： " + longAdder.toString());
    }
}
