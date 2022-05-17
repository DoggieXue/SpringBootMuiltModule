package org.cloudxue.multi.thread.producerandconsumer.store;

import org.cloudxue.common.util.Print;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName SafeDataBuffer
 * @Description 线程安全的数据缓冲区
 *              使用了SafeDataBuffer的实例对象锁作为同步锁
 * 所有的生产、消费动作在执行过程中都需要抢占同一个同步锁，最终结果就是，所有的生产、消费动作都被串行化了
 * @Author xuexiao
 * @Date 2022/3/31 上午11:15
 * @Version 1.0
 **/
public class SafeDataBuffer<T> {
    private static final int MAX_AMOUNT = 10;

    /**
     * 数据缓冲区保存数据的数据结构
     */
    private List<T> dataList = new LinkedList<>();
    /**
     * 数据缓冲区记录存储数据的个数
     */
    private AtomicInteger amount = new AtomicInteger(0);

    public synchronized void add(T element) throws Exception {
        if (amount.get() > MAX_AMOUNT) {
            Print.cfo("队列已经满了");
            return;
        }
        dataList.add(element);
        Print.tcfo(element + "");
        amount.incrementAndGet();

        if (amount.get() != dataList.size()) {
            throw new Exception(amount.get() + "!=" + dataList.size());
        }
    }

    public synchronized T fetch() throws Exception {
        if (amount.get() <= 0 ) {
            Print.cfo("队列已经空了！");
            return null;
        }
        T element = dataList.remove(0);
        Print.tcfo(element + "");
        amount.decrementAndGet();

        if (amount.get() != dataList.size()) {
            throw new Exception(amount.get() + "!=" + dataList.size());
        }

        return element;
    }
}
