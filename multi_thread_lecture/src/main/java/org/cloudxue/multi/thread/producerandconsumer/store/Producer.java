package org.cloudxue.multi.thread.producerandconsumer.store;

import org.cloudxue.common.util.Print;
import org.cloudxue.common.util.ThreadUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName Producer
 * @Description 生产者：每隔200ms向数据缓冲区添加数据
 * @Author xuexiao
 * @Date 2022/3/30 上午11:31
 * @Version 1.0
 **/
public class Producer implements Runnable{
    /**
     * 生产的时间间隔
     */
    private static final int PRODUCE_GAP = 200;

    /**
     * 生产的总次数
     */
    private static final AtomicInteger TURN = new AtomicInteger(0);

    /**
     * 生产者对象编号
     */
    private static final AtomicInteger PRODUCE_NO = new AtomicInteger(1);

    /**
     * 生产的动作
     */
    private Callable action ;
    /**
     * 生产者名字
     */
    private String name;

    /**
     * 生产间隔
     */
    private int gap = PRODUCE_GAP;

    public Producer(Callable action, int gap) {
        this.action = action;
        this.gap = gap;
        this.name = "生产者-" + PRODUCE_NO.incrementAndGet();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object out = action.call();
                if (null != out) {
                    Print.tcfo("第" + TURN.get() + "轮生产：" + out);
                }
                //每轮生产之后，休眠一段时间
//                Thread.sleep(gap);
                ThreadUtil.sleepMilliSeconds(gap);
                TURN.incrementAndGet();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
