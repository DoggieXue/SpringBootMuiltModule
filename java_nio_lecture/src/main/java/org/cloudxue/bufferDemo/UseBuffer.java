package org.cloudxue.bufferDemo;

import java.nio.IntBuffer;

/**
 * @ClassName UseBuffer
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/11/25 上午10:51
 * @Version 1.0
 **/

public class UseBuffer {
    static IntBuffer intBuffer = null;
    public static void allocateTest() {
        intBuffer = IntBuffer.allocate(20);
        System.out.println("--------------after allocate--------------");
        System.out.println("position = " + intBuffer.position());
        System.out.println("limit = " + intBuffer.limit());
        System.out.println("capacity = " + intBuffer.capacity());
    }

    public static void putTest() {
        for (int i = 0; i < 5; i++){
            intBuffer.put(i);
        }
        System.out.println("--------------after put--------------");
        System.out.println("position = " + intBuffer.position());
        System.out.println("limit = " + intBuffer.limit());
        System.out.println("capacity = " + intBuffer.capacity());
    }

    public static void flipTest() {
        intBuffer.flip();
        System.out.println("--------------after flip--------------");
        System.out.println("position = " + intBuffer.position());
        System.out.println("limit = " + intBuffer.limit());
        System.out.println("capacity = " + intBuffer.capacity());
    }

    public static void getTest() {
        //先读取两个元素
        for (int i = 0; i < 2; i++) {
            int j = intBuffer.get();
            System.out.println("j = " + j);
        }
        //输出缓冲区的主要属性
        System.out.println("--------------after get 2 int data--------------");
        System.out.println("position = " + intBuffer.position());
        System.out.println("limit = " + intBuffer.limit());
        System.out.println("capacity = " + intBuffer.capacity());
        //再读3个数据
        for (int i = 0; i < 3; i++) {
            int j = intBuffer.get();
            System.out.println("j = " + j);
        }
        //输出缓冲区的主要属性
        System.out.println("--------------after get 3 int data--------------");
        System.out.println("position = " + intBuffer.position());
        System.out.println("limit = " + intBuffer.limit());
        System.out.println("capacity = " + intBuffer.capacity());
    }

    /**
     * rewind方法实现缓冲区可重复读
     */
    public static void rewindTest() {
        intBuffer.rewind();
        System.out.println("--------------after rewind--------------");
        System.out.println("position = " + intBuffer.position());
        System.out.println("limit = " + intBuffer.limit());
        System.out.println("capacity = " + intBuffer.capacity());
    }

    public static void reRead() {
        for (int i = 0; i < 5; i++) {
            if (i == 2) {
                intBuffer.mark();
            }
            int j = intBuffer.get();
            System.out.println("j = " + j);
        }
        System.out.println("--------------after reRead--------------");
        System.out.println("position = " + intBuffer.position());
        System.out.println("limit = " + intBuffer.limit());
        System.out.println("capacity = " + intBuffer.capacity());
    }

    public static void afterReset() {
        intBuffer.reset();
        //输出换冲区的属性值
        System.out.println("--------------after afterReset--------------");
        System.out.println("position = " + intBuffer.position());
        System.out.println("limit = " + intBuffer.limit());
        System.out.println("capacity = " + intBuffer.capacity());
        //继续读取并输出元素
        for (int i = 2; i < 5; i++) {
            int j = intBuffer.get();
            System.out.println("j = " + j);
        }
    }

    public static void clearTest() {
        System.out.println("--------------after afterClear--------------");
        //清空缓冲区，进入写模式
        intBuffer.clear();
        System.out.println("position = " + intBuffer.position());
        System.out.println("limit = " + intBuffer.limit());
        System.out.println("capacity = " + intBuffer.capacity());
    }

    public static void main(String[] args) {
        allocateTest();
        putTest();
        flipTest();
        getTest();
        rewindTest();
        reRead();
        afterReset();
        clearTest();
    }
}
