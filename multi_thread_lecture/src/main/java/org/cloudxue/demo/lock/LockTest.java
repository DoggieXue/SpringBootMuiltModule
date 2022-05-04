package org.cloudxue.demo.lock;

import org.cloudxue.common.util.Print;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

/**
 * @ClassName LockTest
 * @Description Java中的各种锁测试类
 * @Author xuexiao
 * @Date 2022/4/28 下午2:50
 * @Version 1.0
 **/
public class LockTest {
    @Test
    public void testMockLock() throws InterruptedException {
        //每条线程的执行轮数
        final int TURNS = 1000;
        //线程数
        final int THREADS = 10;

        ExecutorService pool = Executors.newFixedThreadPool(THREADS);

        Lock lock = new SimpleMockLock();
        //倒数闩
        CountDownLatch latch = new CountDownLatch(THREADS);
        long start = System.currentTimeMillis();

        for (int i = 0; i < THREADS; i++) {
            pool.submit(() -> {
                try {
                    for (int j = 0; j < TURNS; j++) {
                        //上送锁参数，执行一次累加
                        IncrementData.lockAndFastIncrease(lock);
                    }
                    Print.tco("本线程累加完毕");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                latch.countDown();
            });
        }
        latch.await();
        float time = (System.currentTimeMillis() - start) / 1000F;
        Print.tcfo("运行时长： " + time);
        Print.tcfo("累加结果： " + IncrementData.sum);
    }
}
