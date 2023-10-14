package org.cloud.xue.redis.jedisPool;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JedisPoolBuilder
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/12/1 3:55 下午
 * @Version 1.0
 **/
@Slf4j
public class JedisPoolBuilder {
    public static final int MAX_TOTAL = 50;

    public static final int MAX_IDLE = 50;

    private static JedisPool pool = null;

    static {
        buildPool();
        hotPool();
    }

    /**
     * 获取Jedis连接
     * @return
     */
    public static Jedis getJedis() {
        return pool.getResource();
    }

    /**
     * 构建JedisPool
     * @return
     */
    private static JedisPool buildPool() {
        if (null == pool) {
            long start = System.currentTimeMillis();
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_TOTAL);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(1000 * 10);
            config.setTestOnBorrow(true);
            pool = new JedisPool(config, "", 6379, 10000);
            long end = System.currentTimeMillis();
            log.info("build JedisPool 耗时：{}ms", end - start);
        }
        return pool;
    }

    /**
     * JedisPool预热
     */
    public static void hotPool() {
        long start = System.currentTimeMillis();
        List<Jedis> minIdleJedisList = new ArrayList<Jedis>();
        Jedis jedis = null;
        for (int i = 0; i < MAX_IDLE; i++) {
            try {
                jedis = pool.getResource();
                minIdleJedisList.add(jedis);
                jedis.ping();
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {

            }
        }

        for (int j = 0; j < MAX_IDLE; j++) {
            try {
                jedis = minIdleJedisList.get(j);
                jedis.close();//将Jedis连接归还到池中
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {

            }
        }

        long end = System.currentTimeMillis();
        log.info("JedisPool 预热耗时：{}ms", end - start);
    }
}
