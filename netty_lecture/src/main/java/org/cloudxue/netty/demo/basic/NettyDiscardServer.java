package org.cloudxue.netty.demo.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.cloudxue.common.util.Logger;

/**
 * @ClassName NettyDiscardServer
 * @Description 第一个Netty服务端程序
 *  读取客户端的输入，并打印,不给客户端任何回复
 * @Author xuexiao
 * @Date 2021/12/6 上午10:44
 * @Version 1.0
 **/
public class NettyDiscardServer {
    private final int serverPort;
    ServerBootstrap b = new ServerBootstrap();

    public NettyDiscardServer (int port) {
        this.serverPort = port;
    }

    public void runServer() {
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();
        try {
            //1、设置反应器轮询组
            b.group(bossLoopGroup, workerLoopGroup);
            //2、设置nio类型的通道
            b.channel(NioServerSocketChannel.class);
            //3、设置监听端口
            b.localAddress(serverPort);
            //4、设置通道参数
            b.option(ChannelOption.SO_KEEPALIVE, true);
            //5、装配子通道流水
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //向子通道（传输通道）流水线添加一个handler处理器
                    socketChannel.pipeline().addLast(new NettyDiscardHandler());
                }
            });
            //6、开始绑定服务器：通过调用sync()同步方法阻塞，直到绑定成功
            ChannelFuture channelFuture = b.bind().sync();
            Logger.info("服务器启动成功，监听端口： " + channelFuture.channel().localAddress());
            //7、等待通道关闭的异步任务结束
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //优雅关闭EventLoopGroup
            workerLoopGroup.shutdownGracefully();
            bossLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyDiscardServer(8899).runServer();
    }
}
