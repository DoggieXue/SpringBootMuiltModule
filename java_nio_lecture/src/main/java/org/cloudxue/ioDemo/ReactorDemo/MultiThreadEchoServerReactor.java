package org.cloudxue.ioDemo.ReactorDemo;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.NioDemoConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName MultiThreadEchoServerReactor
 * @Description 多线程版本反应器模式实例
 * 两个选择器、两个子反应器，每个子反应器负责查询和分发一个选择器的IO事件
 * 服务端的监听通道（ServerSocketChannel）都注册到第一个选择器
 * Socket传输通道（SocketChannel）都注册到第二个选择器
 * 实现了【新连接监听】和【IO读写事件监听】的线程分离
 * @Author xuexiao
 * @Date 2021/11/29 下午3:00
 * @Version 1.0
 **/
@Slf4j
public class MultiThreadEchoServerReactor {
    ServerSocketChannel serverSocketChannel;
    AtomicInteger next = new AtomicInteger(0);
    //选择器数组，引入多个选择器
    Selector[] selectors = new Selector[2];
    //引入多个子反应器
    SubSelector[] subSelectors = null;

    MultiThreadEchoServerReactor() throws IOException {
        //初始化多个选择器
        selectors[0] = Selector.open();
        selectors[1] = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        InetSocketAddress address = new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP, NioDemoConfig.SOCKET_SERVER_PORT);
        serverSocketChannel.socket().bind(address);
        log.info("服务端已开始监听: " + address);
        //第一个选择器，负责监听新连接事件
        SelectionKey sk = serverSocketChannel.register(selectors[0], SelectionKey.OP_ACCEPT);
        //绑定Handler：新连接监控Handler绑定到SelectionKey
        sk.attach(new AcceptorHandler());
        //第一个子反应器，负责第一个选择器的新连接事件分发（而不处理）
        SubSelector subSelector1 = new SubSelector(selectors[0]);
        //第二个子反应器，负责第二个选择器的传输事件分发（而不处理）
        SubSelector subSelector2 = new SubSelector(selectors[1]);
        subSelectors = new SubSelector[]{subSelector1, subSelector2};
    }

    private void startServer() {
        new Thread(subSelectors[0]).start();
        new Thread(subSelectors[1]).start();
    }

    //子反应器，负责事件分发，但不负责事件处理
    class SubSelector implements Runnable {
        final Selector selector;

        SubSelector(Selector selector) {
            this.selector = selector;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey sk = iterator.next();
                        if (null != sk) {
                            dispatch(sk);
                        }
                    }
                    selectionKeys.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void dispatch(SelectionKey sk) {
        Runnable handler = (Runnable) sk.attachment();
        if (null != handler) {
            handler.run();
        }
    }

    class AcceptorHandler implements Runnable {
        @Override
        public void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (null != socketChannel) {
                    log.info("接收到一个新连接");
                    int index = next.get();
                    log.info("选择器的编号： " + index);
                    Selector selector = selectors[index];
                    new MultiThreadEchoHandler(selector, socketChannel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException{
        MultiThreadEchoServerReactor server = new MultiThreadEchoServerReactor();
        server.startServer();
    }
}
