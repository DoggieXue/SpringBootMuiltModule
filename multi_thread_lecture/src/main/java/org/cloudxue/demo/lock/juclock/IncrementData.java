package org.cloudxue.demo.lock.juclock;

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
}
