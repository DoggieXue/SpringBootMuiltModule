package org.cloud.xue.multi.thread.producerandconsumer.store;

import org.cloud.xue.petstore.goods.Goods;
import org.cloud.xue.petstore.goods.IGoods;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName SafePetStore
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/3/31 上午11:24
 * @Version 1.0
 **/
public class SafePetStore {
    private static SafeDataBuffer<IGoods> safeDataBuffer = new SafeDataBuffer<>();

    public static Callable<IGoods> produceAction = () -> {
        IGoods goods = Goods.produceOne();
        try {
            safeDataBuffer.add(goods);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goods;
    };

    //消费者执行的动作
    static Callable<IGoods> consumerAction = () -> {
        IGoods goods = null;
        try {
            goods = safeDataBuffer.fetch();
        } catch (Exception e){
            e.printStackTrace();
        }
        return goods;
    };

    public static void main(String[] args) {
        final int THREAD_TOTAL = 20;

        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_TOTAL);
        for (int i = 0; i < 5; i++) {
            threadPool.submit(new Producer(produceAction, 500));
            threadPool.submit(new Consumer(consumerAction, 1500));
        }
    }
}
