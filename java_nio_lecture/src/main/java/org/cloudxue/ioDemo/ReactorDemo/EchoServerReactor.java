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

/**
 * @ClassName EchoServerReactor
 * @Description 单线程Reactor反应器模式实战
 * 1、设计一个反应器类：EchoServerReactor
 * 2、设计两个处理器类：AcceptorHandler-新连接处理器、EchoHandler-回显处理器
 * @Author xuexiao
 * @Date 2021/11/28 下午9:14
 * @Version 1.0
 **/
@Slf4j
public class EchoServerReactor implements Runnable{

    Selector selector;
    ServerSocketChannel serverSocketChannel;

    public EchoServerReactor() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        InetSocketAddress address = new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP, NioDemoConfig.SOCKET_SERVER_PORT);
        serverSocketChannel.socket().bind(address);
        log.info("服务端已开始监听: " + address);
        SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new AcceptorHandler());
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
                    //Reactor负责Dispatch收到的事件
//                    dispatch(selectionKey);
                    AcceptorHandler handler = (AcceptorHandler) selectionKey.attachment();
                    if (null != handler) {
                        handler.run();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void dispatch(SelectionKey selectionKey) {
        Runnable handler = (Runnable) selectionKey.attachment();
        //调用之前attach绑定到选择键的handler处理器对象
        if (null != handler) {
            handler.run();
        }
    }

    //新连接处理器：将请求转发给EchoHandler处理器
    class AcceptorHandler implements Runnable {

        @Override
        public void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel != null) {
                    log.info("接收到一个连接: " + socketChannel);
                    new EchoHandler(selector, socketChannel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(new EchoServerReactor()).start();
    }
}


