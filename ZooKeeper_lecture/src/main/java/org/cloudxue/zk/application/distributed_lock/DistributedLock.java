package org.cloudxue.zk.application.distributed_lock;

/**
 * @ClassName Lock
 * @Description 分布式锁
 * @Author xuexiao
 * @Date 2022/11/18 3:31 下午
 * @Version 1.0
 **/
public interface DistributedLock {

    /**
     * 加锁
     * @return 是否加锁成功
     */
    boolean lock();

    /**
     * 释放锁
     * @return 是否释放成功
     */
    boolean unlock();
}
