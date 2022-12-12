package org.cloudxue.redis.springJedis;

import org.cloudxue.common.bean.User;
import org.cloudxue.common.util.Logger;
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
}
