package org.cloud.xue.netty.demo.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @ClassName WriteReadTest
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/8 下午5:37
 * @Version 1.0
 **/
@Slf4j
public class WriteReadTest {
    @Test
    public void testWriteRead() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);
        PrintAttribute.print("动作： 分配ByteBuf(9, 100)", buffer);

        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        PrintAttribute.print("动作：写入4个字节（1，2，3，4）", buffer);
        log.info("start=================:get============");
        getByteBuf(buffer);
        PrintAttribute.print("动作： 取数据ByteBuf", buffer);
        log.info("start=================:read============");
        readByteBuf(buffer);
        PrintAttribute.print("动作： 读完ByteBuf", buffer);
    }
    //取字节
    private void readByteBuf(ByteBuf buffer) {
        while (buffer.isReadable()) {
            log.info("取一个字节：" + buffer.readByte());
        }
    }
    //读字节，不改变指针
    private void getByteBuf(ByteBuf buffer) {
        for (int i = 0; i < buffer.readableBytes(); i ++) {
            log.info("读一个字节： " + buffer.getByte(i));
        }
    }
}
