package org.cloudxue.multi.thread.cas;

import org.cloudxue.common.bean.User;
import org.cloudxue.common.util.Print;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName AtomicTest
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/4/11 上午6:14
 * @Version 1.0
 **/
public class AtomicTest {
    @Test
    public void testAtomicInteger() {
        AtomicInteger integer = new AtomicInteger();
    }

    @Test
    public void testAtomicReference() {
        AtomicReference<User> userRef = new AtomicReference<User>();
        User user = new User("1", "张三");
        userRef.set(user);
        Print.tco("userRef is: " + user.toString());

        User updateUser = new User("2", "李四");
        boolean success = userRef.compareAndSet(user, updateUser);
        Print.tco("cas result is :" + success);
        Print.tco("after cas, userRef is: " + userRef.get());
    }

    @Test
    public void testAtomicIntegerFieldUpdater() {
        AtomicIntegerFieldUpdater updater = AtomicIntegerFieldUpdater.newUpdater(User.class, "age");
        User user = new User("1", "张三");
        //getAndIncrement:自增，返回原值
        Print.tco(updater.getAndIncrement(user));
        //getAndAdd：增加某个值，返回原值
        Print.tco(updater.getAndAdd(user, 20));
        //获得属性值
        Print.tco(updater.get(user));
    }
}
