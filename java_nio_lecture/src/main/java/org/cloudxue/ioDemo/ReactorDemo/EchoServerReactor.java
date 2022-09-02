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

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public EchoServerReactor() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        InetSocketAddress address = new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP, NioDemoConfig.SOCKET_SERVER_PORT);
        serverSocketChannel.socket().bind(address);
        log.info("服务端已开始监听: " + address);
        //注册serverSocket的accept新连接接收事件
        SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //将新连接处理器作为附件，绑定到sk选择键
        sk.attach(new AcceptorHandler());
    }

    /**
     * 轮询和分发事件
     */
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();  //阻塞
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    //Reactor负责Dispatch收到的事件
                    dispatch(selectionKey);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dispatch(SelectionKey selectionKey) {
        Runnable handler = (Runnable) selectionKey.attachment();
        //调用之前attach绑定到选择键的handler处理器对象
        if (null != handler) {
            handler.run();
        }
    }

    /**
     * 内部类，新连接处理器：主要职责包括
     * 1、完成新连接的接收工作
     * 2、为新连接创建一个负责数据传输的处理器-EchoHandler
     */
    class AcceptorHandler implements Runnable {

        @Override
        public void run() {
            try {
                //接收新连接
                SocketChannel socketChannel = serverSocketChannel.accept();
                //为新连接创建一个负责数据传输的处理器
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


