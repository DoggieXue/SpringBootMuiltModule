package org.cloudxue.innerlock;

import org.cloudxue.common.util.ByteUtil;
import org.cloudxue.common.util.Print;
import org.openjdk.jol.info.ClassLayout;

/**
 * @ClassName ObjectLock
 * @Description 对象锁，集成JOL打印对象布局
 * @Author xuexiao
 * @Date 2022/5/18 3:39 下午
 * @Version 1.0
 **/
public class ObjectLock {

    private Integer amount = 0;

    public void increase() {
        synchronized (this) {
            amount++;
        }
    }

    public Integer getAmount() {
        return amount;
    }

    /**
     * 输出十六进制、小端模式的hashCode
     * @return
     */
    public String hexHash() {
        int hashCode = this.hashCode();
        //转成小端模式的字节数组
        byte[] hashCode_LE = ByteUtil.int2Bytes_LE(hashCode);

        //转成十六进制形式的字符串
        return ByteUtil.byteToHex(hashCode_LE);
    }

    /**
     * 输出二进制、小端模式的hashCode
     * @return
     */
    public String binaryHash() {
        int hashCode = this.hashCode();

        byte[] hashCode_LE = ByteUtil.int2Bytes_LE(hashCode);

        StringBuffer sb = new StringBuffer();
        for (byte b : hashCode_LE) {
            //转成二进制形式的字符串
            sb.append(ByteUtil.byte2BinaryString(b)).append(" ");
        }

        return sb.toString();
    }

    /**
     * 输出十六进制、小端模式的线程ID
     * @return
     */
    public String hexThreadId() {
        long threadId = Thread.currentThread().getId();

        byte[] threadId_LE = ByteUtil.long2bytes_LE(threadId);

        return ByteUtil.byteToHex(threadId_LE);
    }

    /**
     * 输出二进制、小端模式的线程ID
     * @return
     */
    public String binaryThreadId() {
        long threadId = Thread.currentThread().getId();

        byte[] threadId_LE = ByteUtil.long2bytes_LE(threadId);

        StringBuffer buffer = new StringBuffer();
        for (byte b : threadId_LE) {
            //转成二进制形式的字符串
            buffer.append(ByteUtil.byte2BinaryString(b)).append(" ");
        }

        return buffer.toString();
    }

    public void printSelf() {
        //输出十六进制、小端模式的hashCode
        Print.fo("【小端模式】lock hexHash = " + hexHash());
        //输出二进制、小端模式的hashCode
        Print.fo("【小端模式】lock binaryHash = " + binaryHash());
        //通过JOL工具获取this的对象布局
        String printable = ClassLayout.parseInstance(this).toPrintable();
        //输出对象布局
        Print.fo("lock = " + printable);
    }

    /**
     * 打印对象结构布局信息
     */
    public void printObjectStruct() {
        String printable = ClassLayout.parseInstance(this).toPrintable();
        Print.fo("lock = " + printable);
    }
}
