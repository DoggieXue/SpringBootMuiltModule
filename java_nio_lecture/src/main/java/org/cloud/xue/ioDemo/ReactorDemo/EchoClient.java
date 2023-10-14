package org.cloud.xue.ioDemo.ReactorDemo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.cloud.xue.NioDemoConfig;
import org.cloud.xue.common.util.DateUtil;
import org.cloud.xue.common.util.ThreadUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName EchoClient
 * @Description 单线程Reactor模式客户端
 * @Author xuexiao
 * @Date 2021/11/29 上午11:09
 * @Version 1.0
 **/
@Slf4j
public class EchoClient {
    public void start() throws IOException {
        InetSocketAddress address = new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP, NioDemoConfig.SOCKET_SERVER_PORT);
        //获取通道
        SocketChannel socketChannel = SocketChannel.open(address);
        socketChannel.configureBlocking(false);
        //自旋，等待连接
        while (!socketChannel.finishConnect()) {
            log.info("客户端连接中...");
        }
        log.info("客户端连接成功！");
        //启动接收线程
        Processer processer = new Processer(socketChannel);
        Commander commander = new Commander(processer);
        new Thread(commander).start();
        new Thread(processer).start();
    }

    static class Commander implements Runnable {
        Processer processer;
        Commander(Processer processer) {
            //初始化Reactor
            this.processer = processer;
        }
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                ByteBuffer buffer = processer.getSendBuffer();
                Scanner scanner = new Scanner(System.in);
                while (processer.hasData.get()) {
                    log.info("还有消息没有发送完，请稍等...");
                    //休眠一秒
                    ThreadUtil.sleepMilliSeconds(1000);
                }
                log.info("请输入发送内容: ");
                if (scanner.hasNext()) {
                    String next = scanner.next();
                    buffer.put((DateUtil.getNow() + " >>>> " + next).getBytes());
                    processer.hasData.set(true);
                }
            }
        }
    }

    @Data
    static class Processer implements Runnable {
        ByteBuffer sendBuffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
        ByteBuffer readBuffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);

        protected AtomicBoolean hasData = new AtomicBoolean(false);

        final Selector selector;
        final SocketChannel channel;

        Processer(SocketChannel channel) throws IOException {
            //Reactor初始化
            selector = Selector.open();

            this.channel = channel;
            channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }


        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isWritable()) {
                            if (hasData.get()) {
                                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                                sendBuffer.flip();
                                //发送数据
                                socketChannel.write(sendBuffer);
                                sendBuffer.clear();
                                hasData.set(false);
                            }
                        }
                        if (selectionKey.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            int length = 0;
                            while ((length = socketChannel.read(readBuffer)) > 0) {
                                readBuffer.flip();
                                log.info("server echo: " + new String(readBuffer.array(), 0, length));
                                readBuffer.clear();
                            }
                        }
                    }
                    selectionKeys.clear();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException{
        new EchoClient().start();
    }
}
