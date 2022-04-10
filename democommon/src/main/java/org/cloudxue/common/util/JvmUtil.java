package org.cloudxue.common.util;

import sun.misc.Unsafe;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;

/**
 * @ClassName JvmUtil
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/4/6 上午9:09
 * @Version 1.0
 **/
public class JvmUtil {

    /**
     * 获取Unsafe实例对象
     * sun.misc.Unsafe类被定义成final类型，因此不允许被继承，其构造方法为private的方法
     * 因此无法在外部对Unsafe进行实例化，可以通过反射的方式，自定义获取Unsafe实例
     *
     * @return
     */
    public static Unsafe getUnsafe() {
        try {
            //theUnsafe为sun.misc.Unsafe类中定义的私有变量private static final Unsafe theUnsafe
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    /**
     * 获取JVM运行时的线程ID
     * @return
     */
    public static final int getProcessID() {
        //  ManagementFactory是一个在运行时管理和监控Java VM的工厂类
        //  它能提供很多管理VM的静态接口的运行时实例，比如RuntimeMXBean
        //  RuntimeMXBean是Java虚拟机的运行时管理接口.
        //  取得VM运行管理实例，到管理接口句柄
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        //  取得VM运行管理实例的名称，也是JVM运行实例的名称
        String jvmInstanceName = runtimeMXBean.getName();
        return Integer.valueOf(jvmInstanceName.split("@")[0]).intValue();
    }

    /**
     * 取得当前线程名称
     *
     * @return
     */
    public static String curThreadName() {
        return Thread.currentThread().getName();
    }

    /**
     * 获取对象的内存地址
     * @param objects
     * @return
     */
    public static String getAddresses(Object... objects) {
        StringBuffer sb = new StringBuffer();
        sb.append("0x");
        // sun.arch.data.model=32 // 32 bit JVM
        // sun.arch.data.model=64 // 64 bit JVM
        boolean is64bit = Integer.parseInt(System.getProperty("sun.arch.data.model")) == 32 ? false : true;
        Unsafe unsafe = getUnsafe();
        long last = 0;
        int offset = unsafe.arrayBaseOffset(objects.getClass());
        int scale = unsafe.arrayIndexScale(objects.getClass());
        switch (scale) {
            case 4:
                long factor = is64bit ? 8 : 1;
                final long i1 = (unsafe.getInt(objects, offset) & 0xFFFFFFFFL) * factor;
                // 输出指针地址
                sb.append(Long.toHexString(i1));
                last = i1;
                for (int i = 1; i < objects.length; i++) {
                    final long i2 = (unsafe.getInt(objects, offset + i * 4) & 0xFFFFFFFFL) * factor;
                    if (i2 > last)
                        sb.append(", +" + Long.toHexString(i2 - last));
                    else
                        sb.append(", -" + Long.toHexString(last - i2));
                    last = i2;
                }
                break;
            case 8:
                throw new AssertionError("Not supported");
        }
        return sb.toString();
    }
}
