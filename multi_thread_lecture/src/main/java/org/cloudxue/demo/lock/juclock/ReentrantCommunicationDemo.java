package org.cloudxue.demo.lock.juclock;

import org.cloudxue.common.util.Print;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName ReentrantCommunicationDemo
 * @Description 基于显示锁的等待-通知Demo
 * @Author xuexiao
 * @Date 2022/6/18 8:50 上午
 * @Version 1.0
 **/
public class ReentrantCommunicationDemo {
    //创建显示锁
    static Lock lock = new ReentrantLock();
    //获取显示锁绑定的Condition对象
    private static Condition condition = lock.newCondition();

    //等待异步任务
    public static class WaitTarget implements Runnable {
        @Override
        public void run() {
            lock.lock();
            try{
                Print.tcfo("我是等待方...");
                condition.await();
                Print.tcfo("收到通知继续执行...");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    //通知异步任务

    public static class NotifyTarget implements Runnable {
        @Override
        public void run() {
            lock.lock();
            try {
                Print.tcfo("我是通知方...");
                condition.signal();
                Print.tcfo("通知已发出，再等一会儿，完全释放锁后，等待方才可以执行");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock(); //通知方完全释放锁
            }
        }
    }

    public static void main(String[] args) throws InterruptedException{
        Thread waitThread = new Thread(new WaitTarget(), "WaitThread");
        waitThread.start();

        Thread.sleep(1000);

        Thread notifyThread = new Thread(new NotifyTarget(), "NotifyThread");
        notifyThread.start();
    }
}
