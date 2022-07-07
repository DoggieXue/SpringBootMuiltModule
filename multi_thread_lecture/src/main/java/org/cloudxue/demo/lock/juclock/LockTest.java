package org.cloudxue.demo.lock.juclock;

import org.cloudxue.common.util.Print;
import org.cloudxue.common.util.ThreadUtil;
import org.cloudxue.demo.lock.juclock.custom.CLHLock;
import org.cloudxue.demo.lock.juclock.custom.SimpleMockLock;
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

    @Test
    public void testCLHLockCapability() {
        final int THREADS = 10;
        final int TURNS = 100000;

        ExecutorService pool = Executors.newFixedThreadPool(THREADS);

        Lock lock = new CLHLock("CLH锁");
        CountDownLatch latch = new CountDownLatch(THREADS);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < THREADS; i++) {
            pool.submit(() -> {
                for (int j = 0; j < TURNS; j++) {
                    IncrementData.lockAndFastIncrease(lock);
                }
                Print.tcfo("本次累加执行完毕");
                latch.countDown();
            });
        }

        //等待所有线程执行完毕
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        float time = (System.currentTimeMillis() - startTime) / 1000F;
        Print.tcfo("运行时间： " + time);
        Print.tcfo("累加结果： " + IncrementData.sum);
    }

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

    /**
     * 自定义独占锁-SimpleMockLock测试用例
     * 10个线程各自累加1000次求和
     * @throws InterruptedException
     */
    @Test
    public void testMockLock() throws InterruptedException {
        //每条线程的执行轮数
        final int TURNS = 1000;
        //线程数
        final int THREADS = 10;
        //线程池
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        //自定义独占锁
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

    /**
     * 可中断抢锁测试案例
     */
    @Test
    public void testInterruptLock() throws InterruptedException{
        //可重入锁，默认非公平锁
        Lock lock = new ReentrantLock();

        Runnable runnable = () -> {
            IncrementData.lockInterruptiblyAndIncrease(lock);
        };

        Thread t1 = new Thread(runnable, "线程1");
        Thread t2 = new Thread(runnable, "线程2");

        t1.start();
        t2.start();

        ThreadUtil.sleepMilliSeconds(1000);
        Print.synTco("等待100毫秒，中断两个线程");

        t1.interrupt();
        t2.interrupt();

        Thread.sleep(Integer.MAX_VALUE);
    }
}
