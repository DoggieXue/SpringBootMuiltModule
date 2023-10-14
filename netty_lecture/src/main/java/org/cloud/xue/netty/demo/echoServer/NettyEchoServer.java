package org.cloud.xue.netty.demo.echoServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.cloud.xue.netty.demo.NettyDemoConfig;

/**
 * @ClassName NettyEchoServer
 * @Description Netty 实现回显服务器-服务端
 *      - 读取客户端输入的数据
 *      - 将数据回显到Console控制台
 * @Author xuexiao
 * @Date 2021/12/13 上午9:30
 * @Version 1.0
 **/
@Slf4j
public class NettyEchoServer {
    private final int serverPort;
    ServerBootstrap bootstrap = new ServerBootstrap();

    public NettyEchoServer (int serverPort) {
        this.serverPort = serverPort;
    }

    public void runServer() {
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();
        try {
            //1、设置reactor线程组
            bootstrap.group(bossLoopGroup, workerLoopGroup);
            //2、设置NIO类型的Channel
            bootstrap.channel(NioServerSocketChannel.class);
            //3、设置监听端口
            bootstrap.localAddress(serverPort);
            //4、设置通道参数
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            //5、装配子流水线
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                //有连接到达时，会创建一个channel
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    //向子channel流水线添加一个Handler处理器
                    ch.pipeline().addLast(NettyEchoServerHandler.INSTANCE);
                }
            });
            //6、开始绑定server
            ChannelFuture future = bootstrap.bind().sync();
            log.info("服务器启动成功，监听端口： " + future.channel().localAddress());
            //7、等待通道关闭的异步任务结束
            //服务监听通道会一直等待通道关闭的异步任务结束
            ChannelFuture closeFuture = future.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerLoopGroup.shutdownGracefully();
            bossLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyEchoServer(NettyDemoConfig.SOCKET_SERVER_PORT).runServer();
    }
}
