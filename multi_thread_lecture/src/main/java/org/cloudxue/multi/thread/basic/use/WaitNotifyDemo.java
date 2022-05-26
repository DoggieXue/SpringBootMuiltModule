package org.cloudxue.multi.thread.basic.use;

import org.cloudxue.common.util.Print;
import org.cloudxue.common.util.ThreadUtil;

/**
 * @ClassName WaitNotifyDemo
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/5/25 5:49 下午
 * @Version 1.0
 **/
public class WaitNotifyDemo {
    static Object lockObj = new Object();

    //等待线程的异步目标任务
    static class WaitTarget implements Runnable {

        @Override
        public void run() {
            synchronized (lockObj) {
                try {
                    Print.tco("启动等待...");
                    //
                    lockObj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //获取到监视器的Owner权利
                Print.tco("收到通知，当前线程继续执行...");
            }
        }
    }

    //通知线程的异步目标任务
    static class NotifyTarget implements Runnable {

        @Override
        public void run() {
            synchronized (lockObj) {
                //从屏幕读取输入，阻塞通知线程，方便使用jstack查看线程状态
                Print.consoleInput();
                //获取锁，然后进行发送
                lockObj.notifyAll();//此时不会立即释放lockObj的Monitor的Owner，需要执行完毕
                Print.tco("发出通知了，但是线程还没有立马释放锁！");
            }
        }
    }

    public static void main(String[] args) {
        Thread waitThread = new Thread(new WaitTarget(), "WaitThread");
        waitThread.start();
        ThreadUtil.sleepSeconds(1);

        Thread notifyThread = new Thread(new NotifyTarget(), "NotifyThread");
        notifyThread.start();
    }
}
