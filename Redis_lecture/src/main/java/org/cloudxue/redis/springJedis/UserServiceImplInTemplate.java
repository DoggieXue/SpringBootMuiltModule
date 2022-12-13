package org.cloudxue.redis.springJedis;

import org.cloudxue.common.bean.User;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @ClassName UserServiceImplInTemplate
 * @Description RedisCallback回调完成CRUD
 * RedisTemplate提供了对Redis五种数据操作的方法
 * RedisConnection只提供底层的、二进制的层面的Redis操作，Key、Value都是二进制字节数组
 * RedisTemplate是在RedisConnection的基础上，使用可配置的序列化和反序列化工具类，完成上层Redis操作
 * 若不需要序列化、反序列化操作Redis，需要直接使用RedisConnection去操作Redis，则可以使用RedisCallback来实现
 * @Author xuexiao
 * @Date 2022/12/12 4:12 下午
 * @Version 1.0
 **/
public class UserServiceImplInTemplate implements UserService{

    public static final String USER_ID_PREFIX = "user:id:";

    private RedisTemplate redisTemplate;

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Redis缓存有效时间  4分钟
     */
    private static final long CACHE_LONG = 60 * 4;

    private byte[] serializeValue(User s) {
        return redisTemplate.getValueSerializer().serialize(s);
    }

    private byte[] serializeKey(String s) {
        return redisTemplate.getKeySerializer().serialize(s);
    }

    private User deSerializeValue(byte[] b) {
        return (User) redisTemplate.getValueSerializer().deserialize(b);
    }

    /**
     * CRUD - 查询
     * @param id
     * @return
     */
    @Override
    public User getUser(long id) {
        //首先从缓存中获取
        User user = (User) redisTemplate.execute(new RedisCallback<User>() {
            @Override
            public User doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] key = serializeKey(USER_ID_PREFIX + id);
                if (redisConnection.exists(key)) {
                    byte[] value = redisConnection.get(key);
                    return deSerializeValue(value);
                }
                return null;
            }
        });
        //缓存中没有，查询数据库
        if (null == user) {
            //查询数据库，若存在，则同步缓存
        }

        return user;
    }

    /**
     * CRUD - 保存/更新
     * @param user
     * @return
     */
    @Override
    public User saveUser(User user) {
        redisTemplate.execute(new RedisCallback<User>() {
            @Override
            public User doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] key = serializeKey(USER_ID_PREFIX + user.getUid());
                redisConnection.set(key, serializeValue(user));
                redisConnection.expire(key,CACHE_LONG);
                return user;
            }
        });
        //同步数据库操作
        return user;
    }

    /**
     * CRUD - 删除
     * @param id
     */
    @Override
    public void deleteUser(long id) {
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] key = serializeKey(USER_ID_PREFIX + id);
                if (redisConnection.exists(key)) {
                    redisConnection.del(key);
                }
                return true;
            }
        });
    }

    @Override
    public void deleteAll() {

    }
}
