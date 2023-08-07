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
     * @return
     */
    public static String getCurrentTime() {
        return dateFormat("yyyy-MM-dd HH:MM:SS");
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
