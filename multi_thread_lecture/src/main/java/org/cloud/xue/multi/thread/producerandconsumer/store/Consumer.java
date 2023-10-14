package org.cloud.xue.multi.thread.producerandconsumer.store;

import org.cloud.xue.common.util.Print;
import org.cloud.xue.common.util.ThreadUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName Consumer
 * @Description 消费者：默认每隔100ms从数据缓冲区消费数据
 * @Author xuexiao
 * @Date 2022/3/30 上午11:31
 * @Version 1.0
 **/
public class Consumer implements Runnable{
    /**
     * 默认的消费时间间隔
     */
    private static final int CONSUMER_GAP = 100;

    /**
     * 消费总次数
     */
    private static final AtomicInteger TURN = new AtomicInteger(0);
    /**
     * 消费者对象编号
     */
    private static final AtomicInteger CONSUMER_NO = new AtomicInteger(1);
    /**
     * 消费动作
     */
    private Callable action;
    /**
     * 消费者名称
     */
    private String name;
    /**
     * 消费时间间隔
     */
    private int gap = CONSUMER_GAP;

    public Consumer(Callable action, int gap) {
        this.action = action;
        this.gap = gap;
        this.name = "消费者-" + CONSUMER_NO.incrementAndGet();
    }

    @Override
    public void run() {
        while (true) {
            //增加消费次数
            TURN.incrementAndGet();
            try {
                Object out = action.call();
                if (null != out) {
                    Print.tcfo("第" + TURN.get() + "轮消费： " + out);
                }
                ThreadUtil.sleepMilliSeconds(gap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
