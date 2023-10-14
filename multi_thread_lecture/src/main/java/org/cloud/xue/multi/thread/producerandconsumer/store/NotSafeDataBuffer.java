package org.cloud.xue.multi.thread.producerandconsumer.store;

import org.cloud.xue.common.util.Print;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName NotSafeDataBuffer
 * @Description 非安全的版本的数据缓冲区
 * @Author xuexiao
 * @Date 2022/3/30 上午11:31
 * @Version 1.0
 **/
public class NotSafeDataBuffer<T> {
    /**
     * 数据缓冲区保存数据的数据结构
     */
    private List<T> dataList = new LinkedList<T>();
    /**
     * 记录保存到数据缓冲区中的元素数量
     */
    private AtomicInteger amount = new AtomicInteger(0);

    private static final int MAX_AMOUNT = 10;

    /**
     * 向数据缓冲区添加一个元素
     * @param element
     * @throws Exception
     */
    public void add(T element) throws Exception {
        if (dataList.size() > MAX_AMOUNT) {
            Print.cfo("队列已经满了！");
            return;
        }

        dataList.add(element);
        Print.tcfo(element + "");
        amount.incrementAndGet();

        /**
         * 因线程安全问题，导致缓冲区中的元素个数与添加的个数不一致
         * 抛出异常
         */
        if (dataList.size() != amount.get()) {
            throw new Exception(amount + " != " + dataList.size());
        }
    }

    /**
     * 从数据缓冲区获取一个元素
     * @return
     * @throws Exception
     */
    public T fetch() throws Exception {
        if (amount.get() <= 0) {
            Print.cfo("队列已经空了！");
            return null;
        }

        T element = dataList.remove(0);
        Print.tcfo(element + "");
        amount.decrementAndGet();

        /**
         * 因线程安全问题，导致缓冲区中的元素个数与添加的个数不一致
         * 抛出异常
         */
        if (dataList.size() != amount.get()) {
            throw new Exception(amount + " != " + dataList.size());
        }
        return element;
    }
}
