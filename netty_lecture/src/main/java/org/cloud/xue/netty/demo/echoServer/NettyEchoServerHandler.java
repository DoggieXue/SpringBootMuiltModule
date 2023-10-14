package org.cloud.xue.netty.demo.echoServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName NettyEchoServerHandler
 * @Description Netty回显服务器-服务端处理器
 * @Author xuexiao
 * @Date 2021/12/13 上午9:32
 * @Version 1.0
 **/
@Slf4j
public class NettyEchoServerHandler extends ChannelInboundHandlerAdapter {
    public static final NettyEchoServerHandler INSTANCE = new NettyEchoServerHandler();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf)msg;
        log.info("msg type: " + (in.hasArray() ? "堆内存" : "直接内存") );

        int len = in.readableBytes();
        byte[] bytes = new byte[len];
        in.getBytes(0, bytes);
        log.info("server received: " + new String(bytes, "UTF-8"));

        //异步：写回数据
        log.info("写回前，msg.refCnt：" + in.refCnt());
        ChannelFuture f = ctx.writeAndFlush(msg);
        f.addListener( (ChannelFuture future) -> {
            log.info("写回后，msg.refCnt：" + ((ByteBuf)msg).refCnt());
        });
    }
}
