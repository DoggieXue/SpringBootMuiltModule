package org.cloud.xue.ioDemo.nioDiscardDemo;

import lombok.extern.slf4j.Slf4j;
import org.cloud.xue.NioDemoConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @ClassName NioDiscardServer
 * @Description NIO通信实例
 *              读取客户端通道的输入数据、打印、丢弃
 * @Author xuexiao
 * @Date 2021/11/28 下午7:04
 * @Version 1.0
 **/
@Slf4j
public class NioDiscardServer {
    public static void startServer() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_PORT));
        log.info("服务器启动成功！");

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    //若选择键的IO事件是"连接就绪"事件:服务端收到新的客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    log.info("收到新的客户端连接：" + socketChannel.toString());
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()){
                    //选择键的IO事件是"可读"事件
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    ByteBuffer buffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
                    int readLen = 0;
                    while ((readLen = socketChannel.read(buffer)) > 0) {
                        buffer.flip();
                        //将客户端传输的信息打印
                        log.info(new String(buffer.array(), 0, readLen));
                        buffer.clear();
                    }
                    socketChannel.close();
                }
                iterator.remove();
            }
        }
        serverSocketChannel.close();
    }

    public static void main(String[] args) throws IOException{
        startServer();
    }
}
