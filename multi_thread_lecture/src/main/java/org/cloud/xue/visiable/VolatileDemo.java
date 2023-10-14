package org.cloud.xue.visiable;

import org.cloud.xue.common.util.Print;
import org.cloud.xue.common.util.ThreadUtil;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @ClassName VolatileDemo
 * @Description Volatile不具备原子性
 * @Author xuexiao
 * @Date 2022/6/15 9:34 下午
 * @Version 1.0
 **/
public class VolatileDemo {
    private volatile long value;


    @Test
    public void testVolatileAtomic() throws InterruptedException{
        final int TASK_AMOUNT = 10;

        ExecutorService pool = ThreadUtil.getCpuIntenseTargetThreadPool();

        CountDownLatch latch = new CountDownLatch(TASK_AMOUNT);

        final int TURN = 10000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < TASK_AMOUNT; i++) {
            pool.submit(() -> {
                for(int j = 0; j < TURN; j++) {
                    value++;
                }
                latch.countDown();
            });

        }

        latch.await();

        float time = (System.currentTimeMillis() - start) / 1000F;

        Print.tcfo("运行时长" + time);
        Print.tcfo("累加结果为： " + value);
        Print.tcfo("与预期差值： " + (TASK_AMOUNT* TURN - value));
    }
}
