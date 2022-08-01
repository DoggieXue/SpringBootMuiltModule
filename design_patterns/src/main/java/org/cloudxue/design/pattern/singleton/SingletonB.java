package org.cloudxue.design.pattern.singleton;

/**
 * @ClassName SingletonB
 * @Description 静态内部类实例懒汉模式单例
 *              保证线程安全和性能的同时，写法简单
 * @Author xuexiao
 * @Date 2022/8/1 4:48 下午
 * @Version 1.0
 **/
public class SingletonB {
    //单例模式标配：保持单例的静态成员
    private SingletonB() {}
    //静态内部类
    private static class LazyHolder {
        private static final SingletonB INSTANCE = new SingletonB();
    }
    //获取单例的方法
    public static SingletonB getInstance() {
        return LazyHolder.INSTANCE;
    }
}
