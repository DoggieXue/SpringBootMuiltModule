package org.cloud.xue.common.cache.redis.springJedis;

import org.cloud.xue.common.bean.User;
import org.cloud.xue.common.util.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @ClassName SpringRedisTest
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/12/5 3:03 下午
 * @Version 1.0
 **/
public class SpringRedisTest {

    @Test
    public void testServiceImplByTemplate() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-redis.xml");
        UserService userService = (UserService) context.getBean("serviceImplByTemplate");

        long userId = 1L;
        userService.deleteUser(userId);

        User userInRedis = userService.getUser(userId);
        Logger.info("delete user", userInRedis);

        User user = new User("1", "大米");
        userService.saveUser(user);
        Logger.info("save user", user);

        userInRedis = userService.getUser(userId);
        Logger.info("get user ", userInRedis);

    }

    @Test
    public void testServiceImplInTemplate() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-redis.xml");
        UserService service = (UserService)context.getBean("serviceImplInTemplate");

        long userId = 2L;
        service.deleteUser(userId);

        User userInRedis = service.getUser(userId);
        Logger.info("get User: " + userInRedis);

        User user = new User("2", "foo");
        service.saveUser(user);
        Logger.info("save User ", user);

        userInRedis = service.getUser(userId);
        Logger.info("get User " , userInRedis);
    }

    @Test
    public void testServiceImplByAnno() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring-redis.xml");
        UserService service = (UserService) ac.getBean("userServiceImplByAnno");

        long userId = 3L;
        service.deleteUser(userId);

        User userInRedis = service.getUser(userId);
        Logger.info("get user: ", userInRedis);

        User user = new User();
        user.setUid(userId+"");
        user.setNickName("foo");
        service.saveUser(user);
        Logger.info("save user: ", user);

        userInRedis = service.getUser(userId);
        Logger.info("get user: " , userInRedis);
    }

    /**
     * 缓存注解，删除某个命名空间的缓存
     */
    @Test
    public void testServiceDelAll() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring-redis.xml");
        UserService service = (UserService) ac.getBean("userServiceImplByAnno");
        service.deleteAll();
    }
}
