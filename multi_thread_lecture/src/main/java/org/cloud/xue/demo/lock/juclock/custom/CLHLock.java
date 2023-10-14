package org.cloud.xue.demo.lock.juclock.custom;

import lombok.Data;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @ClassName CLHLock
 * @Description CLH自旋锁实现
 * @Author xuexiao
 * @Date 2022/6/28 3:29 下午
 * @Version 1.0
 **/
public class CLHLock implements Lock {
    /**
     * 当前节点的线程本地变脸
     */
    private ThreadLocal<Node> currNodeLocal = new ThreadLocal<>();

    private String name;

    /**
     * CLHLock队列的尾部指针，使用AtomicReference，方便进行CAS自旋
     */
    private AtomicReference<Node> tail = new AtomicReference();

    public CLHLock() {
        tail.getAndSet(Node.EMPTY);
    }

    public CLHLock(String name) {
        this.name = name;
        tail.getAndSet(Node.EMPTY);
    }

    @Override
    public void lock() {
        Node currNode = new Node(true, null);
        Node preNode = tail.get();
        //CAS自旋，直到当前节点插入队列尾部
        while (!tail.compareAndSet(preNode, currNode)) {
            preNode = tail.get();
        }

        //设置前驱
        currNode.setPreNode(preNode);

        //自旋监听前驱节点的locked变量，直到false
        while (currNode.getPreNode().isLocked()) {
            Thread.yield();
        }

        //能执行到此处，说明当前线程已经获取到锁了
//        Print.tcfo("获取到锁了！！");

        //将当前节点缓存在线程本地变量中，用于释放锁
        currNodeLocal.set(currNode);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        //获取当前节点
        Node currNode = currNodeLocal.get();
        //当前节点的前驱置空，方便垃圾回收（前驱节点）
        currNode.setPreNode(null);
        //清空本地线程
        currNodeLocal.set(null);
        //当前节点让出锁，方便下一次抢锁
        currNode.setLocked(false);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Data
    static class Node {
        /**
         * true：当前线程正在抢占锁或已占有锁
         * false：当前线程已释放锁，下一个线程可以抢占锁了
         */
        volatile boolean locked;
        /**
         * 前驱结点
         */
        Node preNode;

        public Node(boolean locked, Node preNode) {
            this.locked = locked;
            this.preNode = preNode;
        }

        /**
         * 初始空节点
         */
        public static final Node EMPTY = new Node(false, null);
    }
}
