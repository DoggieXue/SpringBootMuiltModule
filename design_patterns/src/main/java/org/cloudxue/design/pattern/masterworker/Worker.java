package org.cloudxue.design.pattern.masterworker;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @ClassName Worker
 * @Description 高并发设计模式-Master-Worker模式
 *
 * @Author xuexiao
 * @Date 2022/5/10 下午4:01
 * @Version 1.0
 **/
public class Worker<T extends Task, R> {
    //接收任务的阻塞队列
    private LinkedBlockingDeque<T> taskQueue = new LinkedBlockingDeque<>();
    //worker任务编号
    static AtomicInteger index = new AtomicInteger(1);
    private int workerId;
    //执行任务的线程
    private Thread thread = null;

    public Worker() {
        this.workerId = index.getAndIncrement();
        thread = new Thread(() -> this.run());
        thread.start();
    }

    /**
     * 轮询执行任务
     */
    public void run() {
        for (; ;) {
            try {
                T task = this.taskQueue.take();
                task.setWorkerId(workerId);
                task.execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 接收任务放入异步队列
     * @param task 接收到的任务
     * @param action 设置任务的回调方法
     */
    public void submit(T task, Consumer<R> action) {
        task.resultAction = action;
        try {
            this.taskQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
