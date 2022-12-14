package org.cloudxue.redis.springJedis;

import org.cloudxue.common.bean.User;
import org.cloudxue.common.util.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserServiceImplByAnno
 * @Description 基于Spring缓存注解实现CRUD
 * @Author xuexiao
 * @Date 2022/12/14 10:13 上午
 * @Version 1.0
 **/
@Service
@CacheConfig(cacheNames="userCache")
public class UserServiceImplByAnno implements UserService{

    public static final String USER_UID_PREFIX = "'userCache:'+";

    @Cacheable(key = USER_UID_PREFIX + "T(String).valueOf(#id)")
    @Override
    public User getUser(long id) {
        //若缓存中没有，就从数据库加载
        Logger.info("user is null");
        return null;
    }

    @CachePut(key = USER_UID_PREFIX + "#user.uid")
    @Override
    public User saveUser(User user) {
        Logger.info("user save to redis");
        return user;
    }

    @CachePut(key = USER_UID_PREFIX + "'#user.uid", condition = "T(Long).valueOf(#user.uid)>1000")
    public User saveUserWithCondition(User user) {
        Logger.info("user save to redis");
        return user;
    }

    @CacheEvict(key = USER_UID_PREFIX + "T(String).valueOf(#id)")
    @Override
    public void deleteUser(long id) {
        Logger.info("delete user :", id);
    }

    @CacheEvict(key = "userCache", allEntries = true)
    @Override
    public void deleteAll() {

    }
}
