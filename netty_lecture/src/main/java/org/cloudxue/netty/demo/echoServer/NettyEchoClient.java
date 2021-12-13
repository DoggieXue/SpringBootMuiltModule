package org.cloudxue.netty.demo.echoServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.common.util.DateUtil;
import org.cloudxue.common.util.Logger;
import org.cloudxue.netty.demo.NettyDemoConfig;

import java.util.Scanner;

/**
 * @ClassName NettyEchoClient
 * @Description Netty实现回显服务器-客户端
 * @Author xuexiao
 * @Date 2021/12/13 上午9:31
 * @Version 1.0
 **/
@Slf4j
public class NettyEchoClient {
    private int serverPort;
    private String serverIp;
    Bootstrap bootstrap = new Bootstrap();

    public NettyEchoClient (String ip, int port) {
        this.serverIp = ip;
        this.serverPort = port;
    }

    public void runClient () {
        //创建reactor线程组
        EventLoopGroup workerLoopGrpup = new NioEventLoopGroup();
        try {
            //1、设置reactor线程组
            bootstrap.group(workerLoopGrpup);
            //2、设置nio类型的channel
            bootstrap.channel(NioSocketChannel.class);
            //3、设置监听端口
            bootstrap.remoteAddress(serverIp, serverPort);
            //4、设置通道参数
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            //5、装配通道流水线
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    channel.pipeline().addLast(NettyEchoClientHandler.INSTANCE);
                }
            });
            //
            ChannelFuture f = bootstrap.connect();
            f.addListener(future -> {
                if (future.isSuccess()) {
                    log.info("EchoClient客户端连接成功！");
                } else {
                    log.info("EchoClient客户端连接失败！");
                }
            });
            //阻塞，直到连接完成
            f.sync();
            Channel channel = f.channel();

            Scanner scanner = new Scanner(System.in);
            Logger.tcfo("请输入发送内容： ");
            while (scanner.hasNext()) {
                String next = scanner.next();
                byte[] bytes = (DateUtil.getNow() + ">>>" + next).getBytes("UTF-8");
                ByteBuf buf = channel.alloc().buffer();
                buf.writeBytes(bytes);
                channel.writeAndFlush(buf);
                Logger.tcfo("请输入发送内容： ");
            }
            //
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerLoopGrpup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyEchoClient(NettyDemoConfig.SOCKET_SERVER_IP, NettyDemoConfig.SOCKET_SERVER_PORT).runClient();
    }
}
