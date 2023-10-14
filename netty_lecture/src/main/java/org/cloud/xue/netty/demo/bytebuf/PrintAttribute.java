package org.cloud.xue.netty.demo.bytebuf;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName PrintAttribute
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/8 下午5:49
 * @Version 1.0
 **/
@Slf4j
public class PrintAttribute {

    public static void print(String action, ByteBuf b) {
        log.info("after ===========" + action + "============");
        log.info("1.0 isReadable(): " + b.isReadable());
        log.info("1.1 readerIndex(): " + b.readerIndex());
        log.info("1.2 readableBytes(): " + b.readableBytes());
        log.info("2.0 isWritable(): " + b.isWritable());
        log.info("2.1 writerIndex(): " + b.writerIndex());
        log.info("2.2 writableBytes(): " + b.writableBytes());
        log.info("3.0 capacity(): " + b.capacity());
        log.info("3.1 maxCapacity(): " + b.maxCapacity());
        log.info("3.2 maxWritableBytes(): " + b.maxWritableBytes());
    }
}
