package org.cloudxue.zk.application.distributed_lock;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.common.concurrent.ExecuteTask;
import org.cloudxue.common.concurrent.FutureTaskScheduler;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @ClassName ZKLockTest
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/11/18 5:03 下午
 * @Version 1.0
 **/
@Slf4j
public class ZKLockTest {
    private static final int TURNS = 50;

    int count = 0;

    @Test
    public void testZKLock() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(TURNS);
        for (int i = 0; i < TURNS; i++) {
            FutureTaskScheduler.add(new ExecuteTask() {
                @Override
                public void execute() {
                    ZKLock zkLock = new ZKLock();
                    zkLock.lock();
                    //每条线程执行10次共享变量的累加
                    for (int j = 0; j < 20; j++) {
                        count++;
                    }
                    //模拟抢锁后执行业务逻辑花费的时间
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info(Thread.currentThread().getName()+"线程执行完后count值为： " + count);
                    zkLock.unlock();
                    latch.countDown();
                }
            });

        }
        latch.await();
    }
}
