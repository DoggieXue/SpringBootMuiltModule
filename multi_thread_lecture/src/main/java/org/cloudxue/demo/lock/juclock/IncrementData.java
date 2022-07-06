package org.cloudxue.demo.lock.juclock;

import org.cloudxue.common.util.Print;
import org.cloudxue.common.util.ThreadUtil;

import java.util.concurrent.locks.Lock;

/**
 * @ClassName IncrementData
 * @Description 使用锁的公共代码
 * @Author xuexiao
 * @Date 2022/4/28 下午2:46
 * @Version 1.0
 **/
public class IncrementData {
    public static int sum = 0;

    public static void lockAndFastIncrease(Lock lock) {
        //step1: 抢占锁
        lock.lock();
        try {
            //step2：执行临界区代码
            sum++;
        } finally {
            //step3：释放锁
            lock.unlock();
        }
    }

    /**
     * 可中断抢锁
     * @param lock
     */
    public static void lockInterruptiblyAndIncrease(Lock lock) {
        Print.synTco("开始抢锁");
        try {
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            Print.synTco("抢占被中断，抢锁失败");
            return;
        }

        try {
            Print.synTco("抢到锁了，执行临界区代码");
            ThreadUtil.sleepMilliSeconds(1000);
            sum++;
            if (Thread.currentThread().isInterrupted()) {
                Print.synTco("同步执行被中断");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
