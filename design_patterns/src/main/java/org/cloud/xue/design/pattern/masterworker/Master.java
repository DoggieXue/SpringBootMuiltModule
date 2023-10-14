package org.cloud.xue.design.pattern.masterworker;

import org.cloud.xue.common.util.Print;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName Master
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/5/10 下午4:10
 * @Version 1.0
 **/
public class Master<T extends Task, R> {
    //所有worker的集合
    private HashMap<String, Worker<T, R>> workers = new HashMap<>();
    //任务集合
    private LinkedBlockingDeque taskQueue = new LinkedBlockingDeque();
    //任务处理结果集合
    protected Map<String, R> resultMap = new ConcurrentHashMap<>();
    //Master的任务调度线程
    private Thread thread = null;
    //保持最终的和
    private AtomicLong sum = new AtomicLong(0);

    public Master(int workerCount) {

        for (int i = 0; i < workerCount; i++) {
            Worker<T, R> worker = new Worker<>();
            workers.put("子节点：" + i, worker);
        }

        thread = new Thread(() -> this.execute());
        thread.start();
    }

    /**
     * 提交任务
     * @param task
     */
    public void submit(T task) {
        taskQueue.add(task);
    }

    /**
     * 获取worker结果处理的回调函数
     * @param o
     */
    private void resultCallBack(Object o) {
        Task<R> task = (Task<R>) o;

        String taskName = "Worker:" + task.getWorkerId() + "-Task: " + task.getId();
        R result = task.getResult();
        resultMap.put(taskName, result);

        sum.getAndAdd((Integer)result);
    }

    /**
     * 启动所有子任务
     */
    public void execute() {
        for (; ;) {
            for (Map.Entry<String, Worker<T, R>> entry : workers.entrySet()) {
                T task = null;
                try {
                    //获取任务
                    task = (T) this.taskQueue.take();
                    //获取节点
                    Worker worker = entry.getValue();
                    //分配任务
                    worker.submit(task, this::resultCallBack);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取最终的结果
     */
    public void printResult() {
        Print.tco("++++++ sum is: " + sum.get());
        for (Map.Entry<String, R> entry : resultMap.entrySet()) {
            String taskName = entry.getKey();
            Print.fo(taskName + ": " + entry.getValue());
        }
    }
}
