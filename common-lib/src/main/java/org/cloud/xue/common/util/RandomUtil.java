package org.cloud.xue.common.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName RandomUtil
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/3/30 下午2:21
 * @Version 1.0
 **/
public class RandomUtil {
    /**
     * 按照范围，生成随机的整数
     *
     * @param mod
     * @return [1, mod]之间的整数
     */
    public static int randInMod(int mod) {
        return Math.abs(ThreadLocalRandom.current().nextInt(mod)) + 1;
    }

    /**
     * 按照范围，生成随机的整数
     *
     * @param mod
     * @return [0, mod)之间的整数
     */
    public static int randInModLower(int mod) {
        return Math.abs(ThreadLocalRandom.current().nextInt(mod));
    }

    /**
     * 按照上下限范围，生成随机的整数
     *
     * @param low  下限
     * @param high 上限
     * @return
     */
    public static int randInRange(int low, int high) {
        return randInMod(high - low) + low;
    }
}
