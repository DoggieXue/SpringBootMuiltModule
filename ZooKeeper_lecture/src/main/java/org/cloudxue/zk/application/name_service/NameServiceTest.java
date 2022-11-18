package org.cloudxue.zk.application.name_service;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName NameServiceTest
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/11/17 2:39 下午
 * @Version 1.0
 **/
@Slf4j
public class NameServiceTest {

    public static void main(String[] args) throws InterruptedException {
        long workerId = DistributedIDMaker.instance.getNodeId();
        log.info("当前workerId为：{}", workerId);
        SnowFlakeIdGenerator.instance.init(workerId);

        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        final HashSet idSet = new HashSet();
        Collections.synchronizedCollection(idSet);

        long start = System.currentTimeMillis();
        int threadCount = 10;
        int turns = 50000;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        //线程池中的每个线程循环5万次生成一个分布式ID，放入HashSet中
        for (int i = 0; i < threadCount; i++) {
            threadPool.submit(() -> {
                for (int j = 0; j < turns; j++) {
                    long id = SnowFlakeIdGenerator.instance.nextId();
                    synchronized (idSet) {
                        if (j % 10000 == 0) {
                            log.info("第 " + j + "个分布式ID为：" + id);
                        }
                        idSet.add(id);
                    }
                }
                countDownLatch.countDown();
            });
        }
//        countDownLatch.await(50000, TimeUnit.MICROSECONDS);
        countDownLatch.await();
        threadPool.shutdown();

        long cost = System.currentTimeMillis() - start;
        log.info("end generate distributedId cost " + cost + "ms!");
    }
}
