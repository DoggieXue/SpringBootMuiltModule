package org.cloud.xue.ioDemo.ReactorDemo;

import lombok.extern.slf4j.Slf4j;
import org.cloud.xue.NioDemoConfig;

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
 * @Description 多线程Reactor反应器模式实例
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
    //boss选择器，负责查询和分发新连接事件
    Selector bossSelector = null;
    //选择器数组，引入多个选择器，负责查询和分发IO传输事件
    Selector[] workSelectors = new Selector[2];

    SubReactor bossReactor = null;
    //引入多个子反应器
    SubReactor[] workReactors = null;

    MultiThreadEchoServerReactor() throws IOException {
        //初始化多个选择器
        bossSelector = Selector.open();
        workSelectors[0] = Selector.open();
        workSelectors[1] = Selector.open();

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        InetSocketAddress address = new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP, NioDemoConfig.SOCKET_SERVER_PORT);
        serverSocketChannel.socket().bind(address);
        log.info("服务端已开始监听: " + address);

        //bossSelector负责监听新连接事件
        SelectionKey sk = serverSocketChannel.register(bossSelector, SelectionKey.OP_ACCEPT);
        //绑定Handler：新连接监听Handler绑定到SelectionKey
        sk.attach(new AcceptorHandler());

        //bossReactor子反应器，处理新连接的bossSelector
        bossReactor = new SubReactor(bossSelector);

        //第一个子反应器，负责一个work选择器的传输事件分发（而不处理）
        SubReactor workReactor0 = new SubReactor(workSelectors[0]);
        //第二个子反应器，负责一个work选择器的传输事件分发（而不处理）
        SubReactor workReactor1 = new SubReactor(workSelectors[1]);
        workReactors = new SubReactor[]{workReactor0, workReactor1};
    }

    private void startServer() {
        //每个子反应器对应一条线程
        new Thread(bossReactor).start();
        new Thread(workReactors[0]).start();
        new Thread(workReactors[1]).start();
    }

    /**
     * 子反应器内部类
     * 负责事件分发，但不负责事件处理
     */
    class SubReactor implements Runnable {
        final Selector selector;

        SubReactor(Selector selector) {
            this.selector = selector;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    selector.select(1000);
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    if (null == selectionKeys || selectionKeys.size() == 0) {
                        continue;
                    }
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
                    Selector selector = workSelectors[index];
                    new MultiThreadEchoHandler(selector, socketChannel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //轮询策略
            if (next.getAndIncrement() == workSelectors.length) {
                next.set(0);
            }
        }
    }

    public static void main(String[] args) throws IOException{
        MultiThreadEchoServerReactor server = new MultiThreadEchoServerReactor();
        server.startServer();
    }
}
