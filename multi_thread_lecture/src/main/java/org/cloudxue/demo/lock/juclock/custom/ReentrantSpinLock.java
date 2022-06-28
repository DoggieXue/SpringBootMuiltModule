package org.cloudxue.demo.lock.juclock.custom;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @ClassName ReentrantSpinLock
 * @Description 基于CAS自旋，实现可重入的自旋锁
 * @Author xuexiao
 * @Date 2022/6/28 10:35 上午
 * @Version 1.0
 **/
public class ReentrantSpinLock implements Lock {
    /**
     * 锁的拥有者
     */
    private AtomicReference owner = new AtomicReference();
    /**
     * 计数器，记录重复获取锁的次数
     * 由于是同一个线程操作，因此无需使用volatile保障可见性和有序性
     */
    private int count = 0;

    @Override
    public void lock() {
        Thread t = Thread.currentThread();
        if (t == owner.get()) {
            count++;
            return;
        }
        //若未获取到锁，则自旋
        while (!owner.compareAndSet(null, t)) {
            //Do nothing
            Thread.yield();
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

    @Override
    public void unlock() {
        Thread t = Thread.currentThread();
        //锁的拥有者才能释放
        if (t == owner.get()) {
            if (count > 0) {//若重入次数大于0，则减少重入次数
                count--;
            } else { //此处不需要compareAndSet，因为已经通过owner做过线程检查
                owner.set(null);
            }
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
