package org.cloud.xue.netty.websocket;

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
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.cloud.xue.common.config.SystemConfig;

/**
 * @ClassName WebSocketEchoServer
 * @Description WebSocket回显服务器
 * @Author xuexiao
 * @Date 2022/10/27 11:24 上午
 * @Version 1.0
 **/
@Slf4j
public class WebSocketEchoServer {

    static class EchoInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new HttpRequestDecoder());
            pipeline.addLast(new HttpResponseEncoder());
            pipeline.addLast(new HttpObjectAggregator(65535));
            //WebSocket报文处理：包括数据压缩与解压
            pipeline.addLast(new WebSocketServerProtocolHandler("/ws","echo", true, 10 *1024));
            //以下是自定义的业务处理器
            pipeline.addLast(new WebPageHandler());//网页处理逻辑
            pipeline.addLast(new TextWebSocketFrameHandler());
        }
    }

    public static void startServer(String ip) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new EchoInitializer());
            Channel channel = bootstrap.bind(SystemConfig.SOCKET_SERVER_PORT).sync().channel();
            log.info("WebSocket服务端已启动 http://{}:{}", ip, SystemConfig.SOCKET_SERVER_PORT);
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
