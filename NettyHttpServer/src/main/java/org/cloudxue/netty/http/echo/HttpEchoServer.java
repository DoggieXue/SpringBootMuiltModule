package org.cloudxue.netty.http.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.common.config.SystemConfig;

/**
 * @ClassName HttpEchoServer
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/10/20 4:06 下午
 * @Version 1.0
 **/
@Slf4j
public class HttpEchoServer {

    static class ChildInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            //Http请求解码器
            pipeline.addLast(new HttpRequestDecoder());
            //Http响应编码器
            pipeline.addLast(new HttpResponseEncoder());
            pipeline.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
            //自定义业务handler
            pipeline.addLast(new HttpEchoHandler());
        }
    }

    public static void startServer() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        try {
            b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).childHandler(new ChildInitializer());
            Channel ch = b.bind(SystemConfig.SOCKET_SERVER_PORT).sync().channel();
            log.info("HTTP ECHO 服务端已经启动 http://{}:{}",
                    SystemConfig.SOCKET_SERVER_NAME,
                    SystemConfig.SOCKET_SERVER_PORT);
            //等待服务端监听到端口关闭
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
