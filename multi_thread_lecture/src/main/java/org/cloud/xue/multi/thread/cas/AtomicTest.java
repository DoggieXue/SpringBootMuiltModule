package org.cloud.xue.multi.thread.cas;

import org.cloud.xue.common.bean.User;
import org.cloud.xue.common.util.Print;
import org.cloud.xue.common.util.ThreadUtil;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.*;

/**
 * @ClassName AtomicTest
 * @Description JUC原子类练习
 * @Author xuexiao
 * @Date 2022/4/11 上午6:14
 * @Version 1.0
 **/
public class AtomicTest {
    /**
     * 基本原子类练习：以AtomicInteger为例
     */
    @Test
    public void testAtomicInteger() {
        int tempValue = 0;
        AtomicInteger i = new AtomicInteger(0);

        //取值并设置一个新值
        i.getAndSet(4);
        System.out.println(i.get());

        //取值并自增
        i.getAndIncrement();
        System.out.println(i.get());

        //取值并增加一个值
        i.getAndAdd(6);
        System.out.println(i.get());
        //比较并交换
        boolean result = i.compareAndSet(10,20);
        System.out.println(result);

        i.compareAndSet(i.get(),20);
        System.out.println(i.get());
    }

    @Test
    public void testAtomicInteger1() throws InterruptedException{

        AtomicInteger amount = new AtomicInteger(0);

        CountDownLatch latch = new CountDownLatch(10);

        //创建10个线程
        for(int i = 0; i < 10; i++) {

            //每个线程执行1000次自增
            ThreadUtil.getMixedTargetThreadPool().submit(() -> {
                for(int j = 0; j < 1000; j++) {
                    amount.getAndIncrement();
                }
                //每个线程执行完一次后，倒数闩减少1次
                latch.countDown();
            });
        }
        //等待10条线程全部执行完毕
        latch.await();
        Print.tco("10条线程分别自增1000次结果： " + amount.get());
    }
    /**
     * 数组原子类练习：以AtomicIntegerArray为例
     */
    @Test
    public void testAtomicIntegerArray() {
        int[] array = {0,1,2,3,4,5,6,7,8,9};
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(array);

        //获取第5个元素并设置为20
        atomicIntegerArray.getAndSet(4, 20);
        Print.tco("数组第5个元素为： " + atomicIntegerArray.get(4));
        //获取第2个元素并自增
        atomicIntegerArray.getAndIncrement(1);
        Print.tco("数组第2个元素为： " + atomicIntegerArray.get(1));
        //获取第8个元素并加6
        atomicIntegerArray.getAndAdd(7, 6);
        Print.tco("数组第8个元素为： " + atomicIntegerArray.get(7));
        //比较并设置第1个元素为44
        boolean result = atomicIntegerArray.compareAndSet(0, 0, 44);
        Print.tco("设置数组第一个元素结果： " + result);
    }

    /**
     * 引用原子类练习：以AtomicReference为例
     */
    @Test
    public void testAtomicReference() {
        AtomicReference<User> userRef = new AtomicReference<User>();
        User user = new User("1", "张三");
        //为原子对象设置值
        userRef.set(user);
        Print.tco("userRef is: " + userRef.get());

        User updateUser = new User("2", "李四");
        boolean success = userRef.compareAndSet(user, updateUser);
        Print.tco("cas result is :" + success);
        Print.tco("after cas, userRef is: " + userRef.get());
    }

    /**
     * 属性更新原子类练习：以AtomicIntegerFieldUpdater为例
     * 1、更新的对象属性必须使用public volatile修饰符
     * 2、由于属性更新原子类都是抽象的，所以每次需要使用newUpdater()静态方法创建一个更新器，并且设置想要更新的类和属性
     */
    @Test
    public void testAtomicIntegerFieldUpdater() {
        AtomicIntegerFieldUpdater updater = AtomicIntegerFieldUpdater.newUpdater(User.class, "age");
        User user = new User("1", "张三");
        //getAndIncrement:自增，返回原值
        Print.tco(updater.getAndIncrement(user));

        //getAndAdd：增加某个值，返回原值
        Print.tco(updater.getAndAdd(user, 20));
        //获得属性值
        Print.tco(updater.get(user));
    }

    /**
     * AtomicStampedReference解决ABA问题
     * @throws InterruptedException
     */
    @Test
    public void testAtomicStampedReference() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        AtomicStampedReference<Integer> atomicStampedRef = new AtomicStampedReference(1, 0);

        ThreadUtil.getMixedTargetThreadPool().submit(() -> {
            boolean success = false;
            int stamp = atomicStampedRef.getStamp();
            Print.tco("before sleep 500ms, value = " + atomicStampedRef.getReference() + ", stamp = " + atomicStampedRef.getStamp());

            ThreadUtil.sleepMilliSeconds(500);

            success = atomicStampedRef.compareAndSet(1, 10, stamp, stamp + 1);
            Print.tco("after sleep 500ms cas 1, success = " + success + " value = " + atomicStampedRef.getReference() + ", stamp = " + atomicStampedRef.getStamp());

            //增加印戳值，然后更新，如果stamp被其他线程改了，则会更新失败
            stamp++;

            success = atomicStampedRef.compareAndSet(10, 1, stamp, stamp + 1);
            Print.tco("after sleep 500ms cas 2, success = " + success + " value = " + atomicStampedRef.getReference() + ", stamp = " + atomicStampedRef.getStamp());

            latch.countDown();
        });

        ThreadUtil.getMixedTargetThreadPool().submit(() -> {
            boolean success = false;
            int stamp = atomicStampedRef.getStamp();
            Print.tco("before sleep 1000ms, value = " + atomicStampedRef.getReference() + ", stamp = " + atomicStampedRef.getStamp());

            ThreadUtil.sleepMilliSeconds(1000);
            Print.tco("after sleep 1000ms, stamp = " + atomicStampedRef.getStamp());

            success = atomicStampedRef.compareAndSet(1, 20, stamp, stamp + 1);
            Print.tco("before sleep 1000ms cas 3, success = " + success + " value = " + atomicStampedRef.getReference() + ", stamp = " + atomicStampedRef.getStamp());

            latch.countDown();
        });
        latch.await();
    }

    /**
     * 使用AtomicMarkableReference解决ABA问题
     * 两个线程更新一个变量
     * @throws InterruptedException
     */
    @Test
    public void testAtomicMarkableReference() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        AtomicMarkableReference<Integer> markableRef = new AtomicMarkableReference<>(1, false);

        ThreadUtil.getMixedTargetThreadPool().submit(new Runnable() {
            @Override
            public void run() {
                boolean success = false;
                int value = markableRef.getReference();
                boolean mark = getMark(markableRef);
                Print.tco("before sleep 500ms, value = " + value + ", mark = " + mark);

                ThreadUtil.sleepMilliSeconds(500);

                success = markableRef.compareAndSet(1, 10, mark, !mark);
                Print.tco("after sleep 500ms cas 1, success = " + success + ", value = " + markableRef.getReference() + ", mark = " + getMark(markableRef));

                latch.countDown();
            }
        });

        ThreadUtil.getMixedTargetThreadPool().submit(new Runnable() {
            @Override
            public void run() {
                boolean success = false;
                int value = markableRef.getReference();
                boolean mark = getMark(markableRef);
                Print.tco("before sleep 1000ms, value = " + value + ", mark = " + mark);

                ThreadUtil.sleepMilliSeconds(1000);
                Print.tco("after sleep 1000ms, mark = " + getMark(markableRef));

                success = markableRef.compareAndSet(1, 20, mark, !mark);
                Print.tco("after sleep 1000ms cas 3, success = " + success + ", value = " + markableRef.getReference() + ", mark = " + getMark(markableRef));

                latch.countDown();
            }
        });
        latch.await();

    }

    private boolean getMark(AtomicMarkableReference<Integer> markableRef) {
        boolean[] markHolder = {false};
        int value = markableRef.get(markHolder);
        return markHolder[0];
    }
}
