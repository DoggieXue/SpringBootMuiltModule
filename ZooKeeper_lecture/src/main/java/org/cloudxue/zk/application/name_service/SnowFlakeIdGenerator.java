package org.cloudxue.zk.application.name_service;

/**
 * @ClassName SnowFlakeIdGenerator
 * @Description 基于ZooKeeper实现雪花算法，生成分布式ID
 * @Author xuexiao
 * @Date 2022/11/17 11:15 上午
 * @Version 1.0
 **/
public class SnowFlakeIdGenerator {
    public static SnowFlakeIdGenerator instance = new SnowFlakeIdGenerator();

    public synchronized void init(long workerId) {
        if (workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException("worker Id is wrong: " + workerId);
        }
        instance.workerId = workerId;
    }

    private SnowFlakeIdGenerator(){

    }

    /**
     * 时间戳，开始使用该算法的时间为：2017-01-01 00:00:00
     */
    private static final long START_TIME = 1483200000000L;
    /**
     * 工作机器id， 占有的bit数，最多支持8192个节点
     */
    private static final int WORKER_ID_BITS = 13;
    /**
     * 序列号，占有的bit数，每毫秒单节点支持的最大ID数 1024
     */
    private static final int SEQUENCE_BITS = 10;

    /**
     * 最大的workerID  8091
     * -1的补码左移13位后取反，结果是：尾部的13位为1，前面为0
     */
    private static final long MAX_WORKER_ID = ~ (-1L << WORKER_ID_BITS);
    /**
     * 最大序列号 1023
     */
    private static final long MAX_SEQUENCE = ~ (-1L << SEQUENCE_BITS);

    /**
     * worker节点编号的移位  10位
     */
    private static final long APP_HOST_ID_SHIFT = SEQUENCE_BITS;
    /**
     * 时间戳的移位   10+13=23位
     */
    private static final long TIMESTAMP_LEFT_SHIFT = WORKER_ID_BITS + APP_HOST_ID_SHIFT;

    /**
     * 该项目的worker节点ID
     */
    private long workerId;
    /**
     * 上次生成ID的时间戳
     */
    private long lastTimestamp = -1L;
    /**
     * 当前毫秒生成的序列值
     */
    private long sequence = 0L;

    /**
     * 对外提供获取分布式ID的方法
     * @return
     */
    public Long nextId() {
        return generateId();
    }

    private synchronized long generateId() {
        long current = System.currentTimeMillis();
        //若当前时间小于上次ID生成的时间戳，表明系统时钟回退过，此时不应该生成ID
        if (current < lastTimestamp) {
            return -1;
        }
        //若当前生成ID的时间还是上次的时间，那么对sequence序列号进行+1
        if (current == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == MAX_SEQUENCE) {
                //当前毫秒生成的序列数已经为最大值，阻塞到下一毫秒
                current = this.nextMs(current);
            }
        }else {
            //当前时间已经是下一个毫秒
            sequence = 0L;
        }
        //更新上次生成ID的时间
        lastTimestamp = current;

        //时间戳左移23位
        long time = (current -START_TIME) << TIMESTAMP_LEFT_SHIFT;
        //workerId左移10位
        long workerId = this.workerId << APP_HOST_ID_SHIFT;
        return time | workerId | sequence;
    }

    /**
     * 阻塞到下一个毫秒
     * @param timeStamp
     * @return
     */
    private long nextMs(long timeStamp) {
        long current = System.currentTimeMillis();
        while (current <= timeStamp) {
            current = System.currentTimeMillis();
        }
        return current;
    }

}
