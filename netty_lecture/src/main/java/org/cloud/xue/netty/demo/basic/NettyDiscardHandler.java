package org.cloud.xue.netty.demo.basic;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.cloud.xue.common.util.Logger;

/**
 * @ClassName NettyDiscardHandler
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/13 下午3:15
 * @Version 1.0
 **/
public class NettyDiscardHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        try {
            Logger.info("收到消息，丢弃如下： ");
            while (in.isReadable()) {
                System.out.println((char) in.readByte());
            }
            System.out.println();//换行
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
