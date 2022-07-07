package org.cloudxue.demo.lock.juclock.custom;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @ClassName SimpleMockLock
 * @Description 基于AQS实现简单的独占锁
 * @Author xuexiao
 * @Date 2022/5/4 上午11:00
 * @Version 1.0
 **/
public class SimpleMockLock implements Lock {

    //自定义同步器
    private final Sync sync = new Sync();

    /**
     * 自定义同步器
     * AbstractQueuedSynchronizer.state  锁状态，0：锁未被占用；1：锁已被占用
     */
    private static class Sync extends AbstractQueuedSynchronizer {

        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0,arg)){
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        /**
         * 锁释放逻辑
         * 1、判断当前线程是否持锁线程
         * 2、判断持锁线程是否持有锁
         * @param arg
         * @return
         */
        @Override
        protected boolean tryRelease(int arg) {
            //若当前线程非占用锁线程
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                //非法状态异常
                throw new IllegalMonitorStateException();
            }
            //若锁未被占用
            if (getState() == 0) {
                //非法状态异常
                throw new IllegalMonitorStateException();
            }

            setExclusiveOwnerThread(null);
            setState(0);

            return true;
        }
    }

    /**
     * 显示锁抢占方法
     * 将节点添加到等待队列的尾部
     */
    @Override
    public void lock() {
//        sync.tryAcquire(1);
        sync.acquire(1);
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
     * 修改队列中当前节点的状态为0，启动后继节点的线程执行
     */
    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
