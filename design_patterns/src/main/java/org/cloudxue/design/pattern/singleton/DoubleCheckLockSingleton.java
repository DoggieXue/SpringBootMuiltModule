package org.cloudxue.design.pattern.singleton;

/**
 * @ClassName DoubleCheckLockSingleton
 * @Description 双重检查锁单例
 *  类锁作为synchronized的同步锁时，会造成同一个JVM内的所有线程，只能互斥进入临界区
 *  synchronized块正确执行完毕，监视锁自动释放；或者程序出现异常，非正常退出synchronized块，监视锁自动释放。
 * @Author xuexiao
 * @Date 2022/6/10 11:21 上午
 * @Version 1.0
 **/
public class DoubleCheckLockSingleton {
    //单例模式标配：保持单例的静态成员
    private static DoubleCheckLockSingleton instance;
    //单例模式标配：私有构造方法
    private DoubleCheckLockSingleton() {}

    public static DoubleCheckLockSingleton getInstance() {
        if (instance == null) { // 第一重检查
            synchronized (DoubleCheckLockSingleton.class) { //使用类锁作为同步锁
                if (instance == null) {// 第二重检查
                    instance = new DoubleCheckLockSingleton();
                }
            }
        }
        return instance;
    }
}
