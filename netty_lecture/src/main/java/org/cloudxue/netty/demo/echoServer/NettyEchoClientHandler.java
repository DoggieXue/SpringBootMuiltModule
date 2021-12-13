package org.cloudxue.netty.demo.echoServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName NettyEchoClientHandler
 * @Description Netty回显服务器-客户端处理器
 * @Author xuexiao
 * @Date 2021/12/13 上午9:33
 * @Version 1.0
 **/
@Slf4j
public class NettyEchoClientHandler extends ChannelInboundHandlerAdapter {
    public static final NettyEchoClientHandler INSTANCE = new NettyEchoClientHandler();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        int len = in.readableBytes();
        byte[] bytes = new byte[len];
        in.getBytes(0, bytes);
        log.info("client recevice: " + new String(bytes, "UTF-8"));
        in.release();
    }
}
