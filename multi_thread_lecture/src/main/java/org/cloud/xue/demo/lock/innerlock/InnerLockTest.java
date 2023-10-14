package org.cloud.xue.demo.lock.innerlock;

import org.cloud.xue.common.util.Print;
import org.junit.Test;
import org.openjdk.jol.vm.VM;

import java.util.concurrent.CountDownLatch;

import static org.cloud.xue.common.util.ThreadUtil.sleepMilliSeconds;

/**
 * @ClassName InnerLockTest
 * @Description Java内置锁测试案例
 * @Author xuexiao
 * @Date 2022/5/19 9:49 上午
 * @Version 1.0
 **/
public class InnerLockTest {

    final int MAX_TREAD = 10;
    final int MAX_TURN = 1000;

    /**
     * JOL打印对象结构
     */
    @Test
    public void showNoLockObject() {
        //输出JVM的信息
        Print.fo(VM.current().details());

        //创建一个对象
        ObjectLock lock = new ObjectLock();
        Print.fo("object status: ");

        lock.printSelf();
    }


    /**
     * 偏向锁案例
     * 偏向锁是针对一个线程而言的，线程获得锁之后就不会再有解锁等操作了，可以省略很多开销
     */
    @Test
    public void showBiasedLock() throws InterruptedException{
        Print.tcfo(VM.current().details());
        //JVM 延迟偏向锁：JVM默认开启偏向锁，但是默认延时4秒开启
        sleepMilliSeconds(5000);

        ObjectLock lock = new ObjectLock();

        Print.tcfo("抢占锁前，lock的状态： ");
        lock.printObjectStruct();

        sleepMilliSeconds(5000);
        CountDownLatch latch = new CountDownLatch(1);
        Runnable runnable = () -> {
            for (int i = 0; i < MAX_TURN; i++) {
                synchronized (lock) {
                    lock.increase();
                    if (i == MAX_TURN / 2) {
                        Print.tcfo("占有锁，lock的状态： ");
                        lock.printObjectStruct();
                    }
                }
                sleepMilliSeconds(10);
            }
            latch.countDown();
        };

        new Thread(runnable, "biased-demo-thread").start();
        latch.await();
        sleepMilliSeconds(5000);
        Print.tcfo("释放锁后，lock的状态： ");
        lock.printObjectStruct();
    }

    /**
     * 轻量级锁案例演示
     */
    @Test
    public void showLightWeightLock() throws InterruptedException{
        Print.tcfo(VM.current().details());
        sleepMilliSeconds(5000);

        ObjectLock lock = new ObjectLock();

        Print.tcfo("抢占锁前，lock 的状态：");
        lock.printObjectStruct();

        sleepMilliSeconds(5000);
        CountDownLatch latch = new CountDownLatch(1);
        Runnable runnable = () -> {
            for (int i = 0; i < MAX_TURN; i++) {
//                Print.tcfo(Thread.currentThread().getName() + "线程，第【" + i + "】次循环...");
                synchronized (lock) {
                    lock.increase();
                    if (i == 1) {
                        Print.tcfo("第一个线程占有锁，lock 的状态： ");
                        lock.printObjectStruct();
                    }
                }
            }
            //循环完毕
//            latch.countDown();

            //线程虽已经释放锁，但一直存在
            for (int j = 0; ; j++) {
                sleepMilliSeconds(1);
            }
        };
        new Thread(runnable,"lightWeight-lock-demo-1").start();

        sleepMilliSeconds(1000);

        Runnable lightWeightRunnable = () -> {
            for (int i = 0; i < MAX_TURN; i++) {
//                Print.tcfo(Thread.currentThread().getName() + "线程，第【" + i + "】次循环...");
                synchronized (lock) {
                    lock.increase();
                    if (i == MAX_TURN / 2) {
                        Print.tcfo("第二个线程占有锁，lock 的状态： ");
                        lock.printObjectStruct();
                    }
                    //每次循环等待1秒
                    sleepMilliSeconds(500);
                }
            }
            latch.countDown();
        };

        new Thread(lightWeightRunnable,"lightWeight-lock-demo-2").start();

        //等待加锁线程执行完毕
        latch.await();
        sleepMilliSeconds(2000);

        Print.tcfo("锁释放后，lock 的状态： ");
        lock.printObjectStruct();
    }

    /**
     * 重量级锁演示
     */
    @Test
    public void showHeavyWeightLock() throws InterruptedException{
        Print.tcfo(VM.current().details());

        sleepMilliSeconds(5000);

        ObjectLock lock = new ObjectLock();
        Print.tcfo("抢占锁前，lock 的状态：");
        lock.printObjectStruct();

        sleepMilliSeconds(5000);

        CountDownLatch latch = new CountDownLatch(3);
        Runnable runnable = () -> {
            for (int i = 0; i < MAX_TURN; i++) {
                synchronized (lock) {
                    lock.increase();
                    if (i == 0) {
                        Print.tcfo("第一个线程占有锁，lock 的状态： ");
                        lock.printObjectStruct();
                    }
                }
            }
            latch.countDown();

            //线程虽然释放锁，但一直存在
            for (int j = 0; ; j++) {
                sleepMilliSeconds(1);
            }
        };
        new Thread(runnable).start();

        sleepMilliSeconds(2000);

        Runnable lightWeightRunnable = () -> {
            for (int i = 0; i < MAX_TURN; i++) {
                synchronized (lock) {
                    lock.increase();
                    if (i == 0 ) {
                        Print.tcfo("占有锁，lock 的状态： ");
                        lock.printObjectStruct();
                    }
                    //临界区循环等待1ms
                    sleepMilliSeconds(1);
                }
            }
            latch.countDown();
        };

        //启动2个线程，开始激烈抢锁
        new Thread(lightWeightRunnable, "抢锁线程1").start();
        sleepMilliSeconds(100);
        new Thread(lightWeightRunnable, "抢锁线程2").start();

        latch.await();
        sleepMilliSeconds(2000);
        Print.tcfo("锁释放后， lock的状态： ");
        lock.printObjectStruct();
    }

}
