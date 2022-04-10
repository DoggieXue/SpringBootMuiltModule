package org.cloudxue.multi.thread.cas;

import org.cloudxue.common.util.JvmUtil;
import org.cloudxue.common.util.Print;
import org.cloudxue.common.util.ThreadUtil;
import sun.misc.Unsafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName OptimisticLockingPlus
 * @Description 基于CAS无锁编程算法，实现轻量级的安全自增
 *              总计10条线程并行运行，每条线程通过CAS自旋对一个共享数据进行自增运算：每条线程成功自增1000次
 *              预期结果：10*1000 = 10000
 * @Author xuexiao
 * @Date 2022/4/10 上午9:42
 * @Version 1.0
 **/
public class OptimisticLockingPlus {
    /**
     * 并发线程数量
     */
    private static final int THREAD_COUNT = 10;
    /**
     * 自增累计值，使用volatile保证线程可见性
     */
    private volatile int value;
    /**
     * Unsafe实例
     */
    private static final Unsafe unSafe = JvmUtil.getUnsafe();
    /**
     * 自增累计值value的内存偏移量
     */
    private static long valueOffset;
    /**
     * 自旋次数统计
     */
    private static final AtomicLong failure = new AtomicLong(0);

    static {
        try {
            valueOffset = unSafe.objectFieldOffset(OptimisticLockingPlus.class.getDeclaredField("value"));
            Print.tco("valueOffset = " + valueOffset);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * CAS原子操作：比较并交换
     * @param oldValue
     * @param newValue
     * @return
     */
    public boolean unSafeCompareAndSet(int oldValue, int newValue) {
        return unSafe.compareAndSwapInt(this, valueOffset, oldValue, newValue);
    }

    /**
     * 安全自增
     */
    public void selfPlus() {
        int i = 0;
        int oldValue ;
        int newValue;
        do {
            //1、获取字段的期望值
            oldValue = value;
            //2、计算出需要替换的新值
            newValue = value + 1;
            if (i++ > 1) {
                failure.incrementAndGet();
            }
            //通过CAS将新值放在字段的内存地址上，若CAS失败，则自旋，重复步骤1、2，直到成功
        } while (!unSafeCompareAndSet(oldValue, newValue));
    }

    public static void main(String[] args) throws InterruptedException {
        final OptimisticLockingPlus cas = new OptimisticLockingPlus();
        //倒数闩：倒数THREAD_COUNT次
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            //向线程池中提交THREAD_COUNT个任务
            ThreadUtil.getMixedTargetThreadPool().submit(() -> {
                //每个任务执行1000次累加
                for (int j = 0; j < 1000; j++) {
                    cas.selfPlus();
                }
                //任务执行完毕后，倒数闩减少一次
                latch.countDown();
            });
        }
        //main线程等待倒数闩执行完毕
        latch.await();
        Print.tco("10条线程并发运行，累加结果：" + cas.value);
        Print.tco("10条线程并发运行，自旋次数：" + cas.failure.get());
    }
}
