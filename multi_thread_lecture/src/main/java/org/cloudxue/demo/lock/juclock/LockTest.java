package org.cloudxue.demo.lock.juclock;

import org.cloudxue.common.util.Print;
import org.cloudxue.common.util.ThreadUtil;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName LockTest
 * @Description Java中的各种显示锁测试类
 * @Author xuexiao
 * @Date 2022/4/28 下午2:50
 * @Version 1.0
 **/
public class LockTest {

    /**
     * ReentrantLock进行同步累加
     */
    @Test
    public void testReentrantLock() {
        final int THREADS = 10;
        final int TURNS = 1000;

        ExecutorService pool = ThreadUtil.getMixedTargetThreadPool();
        Lock lock = new ReentrantLock();
        long start = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(THREADS);

        for (int i = 0; i < THREADS; i++) {
            pool.submit(() -> {
                for (int j = 0; j < TURNS; j++) {
                    IncrementData.lockAndFastIncrease(lock);
                }
                Print.tcfo(Thread.currentThread().getName() + "线程累加完毕");
                latch.countDown();
            });
        }
        try{
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        float time = (System.currentTimeMillis() - start) / 1000F;
        Print.tcfo("运行时长：" + time);
        Print.tcfo("累加结果：" + IncrementData.sum);
    }



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