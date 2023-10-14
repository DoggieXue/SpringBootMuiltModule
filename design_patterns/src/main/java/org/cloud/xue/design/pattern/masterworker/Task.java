package org.cloud.xue.design.pattern.masterworker;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @ClassName Task
 * @Description 异步任务类
 * @Author xuexiao
 * @Date 2022/5/10 下午4:16
 * @Version 1.0
 **/
@Data
public class Task<R> {
    static AtomicInteger index = new AtomicInteger(1);
    //任务的回调函数
    public Consumer<Task<R>> resultAction;
    //任务ID
    private int id;

    private int workerId;
    //计算结果
    R result = null;

    public Task() {
        this.id = index.getAndIncrement();
    }

    public void execute() {
        this.result = doExecute();
        //执行回调
        resultAction.accept(this);
    }

    /**
     * 由子类实现
     * @return
     */
    protected R doExecute() {
        return null;
    }
}
