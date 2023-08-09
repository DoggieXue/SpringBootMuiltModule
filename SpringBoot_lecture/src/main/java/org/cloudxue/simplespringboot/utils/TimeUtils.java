package org.cloudxue.simplespringboot.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName TimeUtils
 * @Description:
 * @Author: Doggie
 * @Date: 2023年08月07日 16:18:28
 * @Version 1.0
 **/
public class TimeUtils {

    /**
     * 获取当前格式化时间
     * @param formatter
     * @return
     */
    private static String dateFormat(String formatter){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(formatter);
        return localDateTime.format(dtf);
    }

    /**
     * 获取当前时间
     * yyyy：年
     * MM：月
     * dd：日
     * hh：1~12小时制(1-12)
     * HH：24小时制(0-23)
     * mm：分
     * ss：秒
     * S：毫秒
     * E：星期几
     * D：一年中的第几天
     * F：一月中的第几个星期(会把这个月总共过的天数除以7)
     * w：一年中的第几个星期
     * W：一月中的第几星期(会根据实际情况来算)
     * a：上下午标识
     * k：和HH差不多，表示一天24小时制(1-24)。
     * K：和hh差不多，表示一天12小时制(0-11)。
     * z：表示时区
     * @return
     */
    public static String getCurrentTime() {
        return dateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    }

    /**
     * 格式化当前时间
     * @param formatter yyyy-MM-dd HH:MM:SS
     * @return
     */
    public static String getCurrentTime(String formatter) {
        return dateFormat(formatter);
    }

    /**
     * 获取当前时瞬时  2019-11-14T02:44:49.725Z
     * @return
     */
    public static Instant getCurrentInstant(){
        return Instant.now();
    }

    /**
     * 判断当前年是否为闰年
     * @return
     */
    public static boolean isLeapYear(){
        return LocalDate.now().isLeapYear();
    }

    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);
        System.out.println(LocalDateTime.now());//UTC/格林威治时间

        System.out.println("===========");
        System.out.println(dateFormat("yyyy-MM-dd HH:MM:SS"));
        System.out.println(dateFormat("HHmmss"));
        System.out.println(getCurrentInstant());
        System.out.println(isLeapYear());
    }
}
