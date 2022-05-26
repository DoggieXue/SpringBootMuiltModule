package org.cloudxue.multi.thread.producerandconsumer.store;

import org.cloudxue.common.util.JvmUtil;
import org.cloudxue.common.util.Print;
import org.cloudxue.petstore.goods.Goods;
import org.cloudxue.petstore.goods.IGoods;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName CommunicatePetStore
 * @Description 基于等待-通知实现生产者消费者模式
 * @Author xuexiao
 * @Date 2022/5/26 11:21 上午
 * @Version 1.0
 **/
public class CommunicatePetStore {
    public static final int MAX_AMOUNT = 10;

    /**
     * 数据缓冲区
     * @param <T>
     */
    static class DataBuffer<T> {
        private List<T> dataList = new LinkedList<>();
        private Integer amount = 0;

        private final Object LOCK_OBJECT = new Object();
        private final Object NOT_FULL = new Object();
        private final Object NOT_EMPTY = new Object();

        public void add (T element) throws Exception {
            while (amount > MAX_AMOUNT) {
                synchronized (NOT_FULL) {
                    Print.tcfo("队列已经满了！");
                    //等待未满通知
                    NOT_FULL.wait();
                }
            }

            //生产一个数据放入缓存区
            synchronized (LOCK_OBJECT) {
                dataList.add(element);
                amount++;
            }

            //上面的临界区已确保生成了一个数据，此处可以确定缓存区数据是非空的
            synchronized (NOT_EMPTY) {//发送未空通知
                NOT_EMPTY.notify();
            }
        }

        public T fetch() throws Exception{

            while (amount <= 0) {
                synchronized (NOT_EMPTY) {
                    Print.tcfo("队列已经空了！");
                    NOT_EMPTY.wait();
                }
            }

            //消费完一个数据
            T element = null;
            synchronized (LOCK_OBJECT) {
                element = dataList.remove(0);
                amount--;
            }

            //上面的临界区已确保消费了一个数据，此处可以确定缓存区数据是未满的
            synchronized (NOT_FULL) {
                NOT_FULL.notify();
            }

            return element;
        }
    }

    public static void main(String[] args) {
        Print.cfo("当前进程的ID是： " + JvmUtil.getProcessID());
        System.setErr(System.out);

        DataBuffer<IGoods> dataBuffer = new DataBuffer<>();

        //生产者动作
        Callable<IGoods> produceAction = () -> {
            IGoods goods = Goods.produceOne();
            dataBuffer.add(goods);
            return goods;
        };

        //消费者动作
        Callable<IGoods> consumerAction = () -> {
            IGoods goods = null;
            goods = dataBuffer.fetch();
            return goods;
        };

        //线程池数
        final int THREAD_TOTAL = 20;
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_TOTAL);

        final int PRODUCE_TOTAL = 1;// 1个生产者
        final int CONSUMER_TOTAL = 10;// 10个消费者

        //每50ms生产一个数据
        for (int i = 0; i < PRODUCE_TOTAL; i++) {
            threadPool.submit(new Producer(produceAction, 50));
        }
        //每100ms消费一个数据
        for (int j = 0; j < CONSUMER_TOTAL; j++) {
            threadPool.submit(new Consumer(consumerAction, 100));
        }
    }
}
