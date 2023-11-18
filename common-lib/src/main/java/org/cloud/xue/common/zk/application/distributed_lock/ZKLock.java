package org.cloud.xue.common.zk.application.distributed_lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.cloud.xue.common.zk.ZooKeeperClient;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName ZKLock
 * @Description 基于ZooKeeper实现分布式锁
 * 原理：通过在一个持久节点下创建临时顺序节点实现。ZooKeeper保证临时顺序节点按次序创建、依次递增
 * 为了保证公平性，规定创建编号最小的那个节点的线程，表示获得了锁
 * @Author xuexiao
 * @Date 2022/11/18 3:33 下午
 * @Version 1.0
 **/
@Slf4j
public class ZKLock implements DistributedLock{
    /**
     * 当前锁的工作节点(永久节点)，加锁前提前创建
     * 通过在该目录下创建临时顺序节点实现加锁
     */
    private static final String ZK_PATH = "/test/lock";
    /**
     * 临时顺序节点的前缀
     */
    private static final String LOCK_PREFIX = ZK_PATH + "/";
    /**
     * 休眠时间
     */
    private static long WAIT_TIME = 1000;
    /**
     * ZooKeeper为临时顺序节点自动创建的节点编号
     * LOCK_PREFIX + locked_short_path = locked_path,即为临时顺序节点的完整路径
     */
    private String locked_short_path = null;
    /**
     * 作为加锁用的节点路径
     * LOCK_PREFIX + locked_short_path = locked_path
     */
    private String locked_path = null;
    /**
     * 前一个节点的路径，用于后续节点监听前一个节点
     */
    private String prior_path = null;
    /**
     * 可重入计数器
     */
    final AtomicInteger lockCount = new AtomicInteger(0);
    /**
     * 用于缓存加锁的线程，用于解锁
     */
    private Thread thread;
    /**
     * Zookeeper操作客户端
     */
    CuratorFramework curatorClient = null;

    public ZKLock () {
        ZooKeeperClient.instance.init();
        synchronized (ZooKeeperClient.instance) {
            if (!ZooKeeperClient.instance.isNodeExist(ZK_PATH)) {
                ZooKeeperClient.instance.createNode(ZK_PATH, Thread.currentThread().getName());
            }
        }
        curatorClient = ZooKeeperClient.instance.getZkClient();
        log.debug("{}线程获得当前的Curator客户端为：{}",Thread.currentThread().getName(), curatorClient);
    }

    @Override
    public boolean lock() {
        //可重入，确保同一线程可重复加锁
        synchronized (this) {

            if (lockCount.get() == 0) { //未有线程抢到锁
                log.info("{}线程开始执行抢锁,lockCount=0",Thread.currentThread().getName());
                thread = Thread.currentThread();
                lockCount.incrementAndGet();
            } else { //已经有持有锁的线程
                log.info("{}线程开始执行抢锁,lockCount = {}",Thread.currentThread().getName(), lockCount.get());
                if (!thread.equals(Thread.currentThread())) {
                    return false;
                }
                lockCount.incrementAndGet();
                return true;
            }
        }
        try {
            boolean locked = false;
            //抢锁
            locked = tryLock();

            //抢到分布式锁
            if (locked) {
                return true;
            }
            //未抢到分布式锁
            log.info("{}线程未抢到分布式锁，开始自旋...",Thread.currentThread().getName());
            while(!locked) {
                //等待抢到锁的线程通知
//                await();
                //判断自己是否为当前子节点列表中编号最小的子节点
                List<String> waiters = getWaiters();
                //判断自己是否为编号最小的节点，若是，则加锁成功
                if (checkLocked(waiters)) {
                    locked = true;
                }
            }
            log.info("{}抢到了分布式锁：{}，自旋结束",Thread.currentThread().getName(), locked_short_path);
            return true;
        } catch (Exception e) {
            lockCount.decrementAndGet();//任何异常导致的获取锁失败，都会减少一次持有锁的数量
            log.error("{}抢锁异常，执行lockCount-1操作, -1后，lockCount={}",Thread.currentThread().getName(), lockCount.get());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean unlock() {
        log.info("开始释放分布式锁，当前加锁线程thread = {}，lockCount = {}", thread.getName(), lockCount);
        // 非加锁线程不可释放锁
        if (!thread.equals(Thread.currentThread())) {
            log.info("非加锁线程释放锁，解锁失败!");
            return false;
        }
        //减少可重入次数
        int newLockCount = lockCount.decrementAndGet();
        log.info("{}线程执行锁释放，lockCount = {}",Thread.currentThread().getName(), lockCount);
        if (newLockCount < 0) {
            throw new IllegalMonitorStateException("Lock count has gone negative for lock:" + locked_path);
        }

        if (newLockCount != 0) {
            return true;
        }

        //删除临时节点
        try {
            if (ZooKeeperClient.instance.isNodeExist(locked_path)) {
                curatorClient.delete().forPath(locked_path);
            }
        } catch (Exception e) {
            log.error("{}删除临时顺序节点{}异常",Thread.currentThread().getName(), locked_path);
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean tryLock() throws Exception{
        //创建临时ZNode
        locked_path = ZooKeeperClient.instance.createNodeWithMode(LOCK_PREFIX, Thread.currentThread().getName().getBytes("UTF-8"), CreateMode.EPHEMERAL_SEQUENTIAL);
        log.info("{}线程创建了一个临时顺序节点： {}",Thread.currentThread().getName(), locked_path);

        if (null == locked_path) {
            throw new Exception("zk error，节点创建失败");
        }
        //获取加锁的排队编号
        locked_short_path = getShortPath(locked_path);

        List<String> waiters = getWaiters();
        //判断所有子节点列表中，自己是否第一个
        if (checkLocked(waiters)) {
            return true;
        }
        //判断自己排第几个
        int index = Collections.binarySearch(waiters, locked_short_path);
        if (index < 0) {//由于网络抖动，获取到的子节点列表中可能已经没有自己了
            throw new Exception("节点未找到：" + locked_short_path);
        }

        //若当前线程未获取到锁，则监听前一个节点
        prior_path = ZK_PATH + "/" + waiters.get(index -1);
        //抢锁失败，就注册监听，而不是在【自旋】阶段监听
        if (null != prior_path) {
            await();
        }
        return false;
    }

    /**
     * locked_path示例：/test/lock/0000000048
     * @param locked_path
     * @return
     */
    private String getShortPath(String locked_path) {
        int index = locked_path.lastIndexOf(LOCK_PREFIX);
        if (index >=0) {
            index += ZK_PATH.length() + 1;
            return index <= locked_path.length() ? locked_path.substring(index) : "";
        }
        return null;
    }



    /**
     * 监听前一个节点的删除事件
     */
    private void await() throws Exception{
        if (null == prior_path) {
            throw new Exception("prior path error");
        }

        final CountDownLatch latch = new CountDownLatch(1);

        //订阅比自己编号小的节点的删除事件
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("监听到{}节点删除", prior_path);
                latch.countDown();
            }
        };
        //这里是个大坑，当前节点监听前一个节点的时候，可能前一个节点已经被删除了,这种情况会造成死循环！！！
        //注册监听  这里不需要反复监听，使用Watcher即可
        curatorClient.getData().usingWatcher(watcher).forPath(prior_path);

        latch.await(WAIT_TIME, TimeUnit.MICROSECONDS);
    }

    /**
     * 是否加锁成功
     * 获取工作节点下的所有子节点
     * @param waiters
     * @return
     */
    private boolean checkLocked(List<String> waiters) {
        //按照节点编号升序排列
        Collections.sort(waiters);

        if (locked_short_path.equals(waiters.get(0))) {
            log.info("{}线程成功抢到分布式锁：{}",Thread.currentThread().getName(), locked_short_path);
            return true;
        }
        return false;
    }

    /**
     * 获取工作节点下所有的子节点列表
     * @return
     */
    private List<String> getWaiters() {
        List<String> children = null;
        try {
            children = curatorClient.getChildren().forPath(ZK_PATH);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return  children;
    }
}
