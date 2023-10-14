package org.cloud.xue.multi.thread.producerandconsumer.store;

import org.cloud.xue.common.util.JvmUtil;
import org.cloud.xue.common.util.Print;
import org.cloud.xue.petstore.goods.Goods;
import org.cloud.xue.petstore.goods.IGoods;

import java.util.concurrent.*;

/**
 * @ClassName ArrayBlockingQueuePetStore
 * @Description 基于阻塞队列ArrayBlockingQueue实现生产者-消费者模式
 * @Author xuexiao
 * @Date 2022/5/6 下午3:27
 * @Version 1.0
 **/
public class ArrayBlockingQueuePetStore {
    /**
     * 数据区长度
     */
    public static final int MAX_AMOUNT = 10;

    /**
     * 数据缓冲区
     * @param <T>
     */
    static class DataBuffer<T> {
        //使用阻塞队列保存缓冲区数据
        private ArrayBlockingQueue<T> dataList = new ArrayBlockingQueue<>(MAX_AMOUNT);

        /**
         * 向数据缓冲区增加一个元素，委托给阻塞队列
         * @param element
         * @throws Exception
         */
        public void add(T element) throws Exception {
            dataList.put(element);
        }

        /**
         * 从数据缓冲区取出一个元素，委托给阻塞队列
         * @return
         * @throws Exception
         */
        public T fetch() throws Exception {
            return dataList.take();
        }
     }

    public static void main(String[] args) {
        Print.cfo("当前进程的ID是： " + JvmUtil.getProcessID());
        System.setErr(System.out);

        DataBuffer<IGoods> dataBuffer = new DataBuffer<>();

        //生产动作
        Callable<IGoods> produceAction = () -> {
            IGoods goods = Goods.produceOne();
            dataBuffer.add(goods);
            return goods;
        };

        //消费动作
        Callable<IGoods> consumerAction = () -> {
            IGoods goods = null;
            goods = dataBuffer.fetch();
            return goods;
        };

        //并发执行线程数
        final int THREAD_TOTAL = 20;

        ExecutorService pool = Executors.newFixedThreadPool(THREAD_TOTAL);

        //假设11条线程，其中1个生产者，10个消费者
        final int CONSUMER_TOTAL = 11;
        final int PRODUCE_TOTAL = 1;

        for (int i = 0; i < PRODUCE_TOTAL; i++) {
            //生产者线程每生成一个商品，间隔50ms
            pool.submit(new Producer(produceAction, 50));
        }

        for (int j = 0; j < CONSUMER_TOTAL; j++) {
            //消费者线程每消费一个商品，间隔100ms
            pool.submit(new Consumer(consumerAction, 100));
        }
    }
}
