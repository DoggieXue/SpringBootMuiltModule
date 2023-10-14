package org.cloud.xue.redis.springJedis;

import lombok.extern.slf4j.Slf4j;
import org.cloud.xue.common.bean.User;

/**
 * @ClassName UserServiceImplByTemplate
 * @Description 使用RedisTemplate模板API完成CRUD案例
 * 1、自定义CacheOperationService，通过封装RedisTemplate模板API供业务Service使用
 * 2、
 * @Author xuexiao
 * @Date 2022/12/2 5:52 下午
 * @Version 1.0
 **/
@Slf4j
public class UserServiceImplByTemplate implements UserService {

    public static final String USER_UID_PREFIX = "user:uid:";

    protected CacheOperationService cacheOperationService;

    public void setCacheOperationService(CacheOperationService cacheOperationService) {
        this.cacheOperationService = cacheOperationService;
    }

    private static final long CACHE_LONG = 60 * 4;


    @Override
    public User getUser(long id) {
        String key = USER_UID_PREFIX + id;
        User value = (User) cacheOperationService.get(key);
        if (null != value) {
            //数据库查询
        }
        return value;
    }

    @Override
    public User saveUser(User user) {
        String key = USER_UID_PREFIX + user.getUid();
        cacheOperationService.set(key, user, CACHE_LONG);
        //保存到数据库...
        return null;
    }

    @Override
    public void deleteUser(long id) {
        String key = USER_UID_PREFIX + id;
        cacheOperationService.del(key);
        log.info("delete User: {}", id);
    }

    @Override
    public void deleteAll() {

    }
}
