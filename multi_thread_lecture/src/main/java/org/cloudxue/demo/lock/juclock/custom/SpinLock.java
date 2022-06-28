package org.cloudxue.demo.lock.juclock.custom;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @ClassName SpinLock
 * @Description 基于CAS自旋的不可重入自旋锁
 * @Author xuexiao
 * @Date 2022/6/28 10:23 上午
 * @Version 1.0
 **/
public class SpinLock implements Lock {

    /**
     * 锁的拥有者
     */
    private AtomicReference<Thread> owner = new AtomicReference<>();

    /**
     * 抢锁
     */
    @Override
    public void lock() {
        Thread t = Thread.currentThread();
        //自旋抢锁：抢锁失败，进入循环，让出当前CPU时间片；抢锁成功，退出该方法
        while (!owner.compareAndSet(null, t)) {
            //Do nothing
            Thread.yield();//让出当前剩余的CPU时间片
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    /**
     *  释放锁
     */
    @Override
    public void unlock() {
        Thread t = Thread.currentThread();
        //拥有者才能释放锁
        if (t == owner.get()) {
            owner.set(null); //此处不需要compareAndSet，因为已经通过owner做过线程检查
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
