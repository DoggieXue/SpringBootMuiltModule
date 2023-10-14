package org.cloud.xue.juc.container;

import org.cloud.xue.common.util.Print;
import org.cloud.xue.common.util.ThreadUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName CopyOnWriteArrayListTest
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/5/6 上午10:20
 * @Version 1.0
 **/
public class CopyOnWriteArrayListTest {

    /**
     * 并发操作的执行目标
     */
    public static class ConcurrentTarget implements Runnable {

        List<String> targetList = null;

        public ConcurrentTarget(List<String> targetList) {
            this.targetList = targetList;
        }

        @Override
        public void run() {
            Iterator<String> iterator = targetList.iterator();
            //遍历targetList
            while (iterator.hasNext()) {
                String threadName = Thread.currentThread().getName();
                Print.tco("开始往同步队列中添加线程名称： " + threadName);
                //遍历过程中，添加元素
                targetList.add(threadName);
            }
        }
    }

    /**
     * 同步队列测试：在迭代操作时，进行列表的修改
     */
    @Test
    public void testSynchronizedList() {
        List<String> notSafeList = Arrays.asList("a","b","c");

        //使用java.util.Collections提供的包装方法，将基础List包装成线程安全的列表容器
        List<String> synList = Collections.synchronizedList(notSafeList);

        ConcurrentTarget synchronizedListDemo = new ConcurrentTarget(synList);

        for (int i = 0; i < 10; i ++) {
            new Thread(synchronizedListDemo, "线程" + i).start();
        }

        ThreadUtil.sleepSeconds(1000);
    }

    /**
     * CopyOnWriteArrayList实现迭代时，往容器中添加数据
     */
    @Test
    public void testCopyOnWriteArrayList() {
        List<String> notSafeList = Arrays.asList("a", "b", "c");
        List<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        copyOnWriteArrayList.addAll(notSafeList);

        ConcurrentTarget copyOnWriteArrayListDemo = new ConcurrentTarget(copyOnWriteArrayList);

        for (int i = 0; i < 10; i++) {
            new Thread(copyOnWriteArrayListDemo, "线程" + i).start();
        }

        ThreadUtil.sleepSeconds(1000);
    }
}
