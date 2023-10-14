package org.cloud.xue.design.pattern.singleton;

/**
 * @ClassName SingletonA
 * @Description DCL+volatile单例
 *              既可以保证性能，又可以保证线程安全
 *              但是底层复杂、写法繁琐
 * @Author xuexiao
 * @Date 2022/8/1 4:46 下午
 * @Version 1.0
 **/
public class SingletonA {
    //单例模式标配：保持单例的静态成员
    public static volatile SingletonA singleton;
    //单例模式标配：私有构造方法
    private SingletonA() {}

    public static SingletonA getInstance() {
        if (null == singleton) {//一重检查
            synchronized (SingletonA.class) {//加锁
                if (null == singleton) { //二重检查
                    singleton = new SingletonA();
                }
            }
        }
        return singleton;
    }
}
