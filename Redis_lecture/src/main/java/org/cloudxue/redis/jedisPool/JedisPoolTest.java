package org.cloudxue.redis.jedisPool;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @ClassName JedisPoolTest
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/12/1 4:33 下午
 * @Version 1.0
 **/
@Slf4j
public class JedisPoolTest {
    public static final int NUM = 200;

    public static final String ZSET_KEY = "zset1";

    @Test
    public void testDel() {
        Jedis redis = null;
        try {
            redis = JedisPoolBuilder.getJedis();
            long start = System.currentTimeMillis();
            redis.del(ZSET_KEY);
            long end = System.currentTimeMillis();
            log.info("删除{} 耗时：{}", ZSET_KEY, end - start);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (null != redis) {
                redis.close();
            }
        }
    }

    /**
     * 由于Jedis实现了java.io.Closeable接口，故可以使用try-with-resources语句
     * 不必再显示调用close()方法来归还Jedis连接
     */
    @Test
    public void testSet() {
        testDel();
        try(Jedis redis = JedisPoolBuilder.getJedis()) {
            int loop = 0;
            long start = System.currentTimeMillis();
            while (loop < NUM) {
                redis.zadd(ZSET_KEY, loop, "field-" + loop);
                loop++;
            }
            long end = System.currentTimeMillis();
            log.info("设置{}: {}, {}次，耗时 {}ms",ZSET_KEY, loop, end-start);
        }
    }
}
