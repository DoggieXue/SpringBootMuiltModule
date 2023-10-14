package org.cloud.xue.concurrent;

import org.cloud.xue.common.util.Logger;

/**
 * @ClassName SyncByJavaJoinDemo
 * @Description 使用Thread的join()方法，实现异步回调,完成泡茶流程
 *              此代码永远不会实际中使用，仅作学习
 * @Author xuexiao
 * @Date 2022/8/8 2:22 下午
 * @Version 1.0
 **/
public class SyncByJavaJoinDemo {
    public static final int SLEEP_GAP = 500;
    public static String getCurrThreadName() {
        return Thread.currentThread().getName();
    }

    /**
     * 清洗线程
     */
    static class WashThread extends Thread {
        public WashThread() {
            super("^^^ 清洗线程 ^^^");
        }

        @Override
        public void run() {
            try {
                Logger.info("洗茶壶");
                Logger.info("洗茶杯");
                //清洗中...
                Thread.sleep(SLEEP_GAP);
                Logger.info("洗完了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Logger.info("运行结束");
        }
    }

    /**
     * 烧水线程
     */
    static class HotWaterThread extends Thread {
        public HotWaterThread() {
            super("*** 烧水线程 ***");
        }

        @Override
        public void run() {
            try {
                Logger.info("灌上凉水");
                Logger.info("开始烧水");
                //烧水中...
                Thread.sleep(SLEEP_GAP);
                Logger.info("水烧开了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Logger.info("运行结束");
        }
    }

    /**
     * 泡茶主线程
     * @param args
     */
    public static void main(String[] args) {
        Thread wThread = new WashThread();
        Thread hThread = new HotWaterThread();
        wThread.start();
        hThread.start();

        try {
            wThread.join();
            hThread.join();
            Thread.currentThread().setName("泡茶主线程");
            Logger.info("万事俱备？泡茶喝");
        } catch (InterruptedException e) {
            Logger.info(getCurrThreadName() + "发生异常被中断");
        }
        Logger.info(getCurrThreadName() + "运行结束");
    }
}
