package org.cloudxue.ioDemo.nioDiscardDemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @ClassName NioServer
 * @Description 服务端接收客户端信息，并将信心广播到所有与之连接的客户端中
 * @Author xuexiao
 * @Date 2021/12/5 下午8:37
 * @Version 1.0
 **/
public class NioServer {
    private static final Map<String, SocketChannel> clients = new HashMap();
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(8899));

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                selectionKeys.forEach(selectionKey -> {
                    final SocketChannel client;
                    try {
                        if (selectionKey.isAcceptable()) {
                            ServerSocketChannel server = (ServerSocketChannel)selectionKey.channel();
                            client = server.accept();
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);

                            String key = "【" + UUID.randomUUID().toString() + "】";

                            clients.put(key, client);
                        } else if (selectionKey.isReadable()) {
                            //从SocketChannel中读取数据
                            client = (SocketChannel) selectionKey.channel();
                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                            //数据从Channel读到Buffer
                            int readLen = client.read(readBuffer);

                            if (readLen > 0) {
                                readBuffer.flip();
                                Charset charset = Charset.forName("UTF-8");
                                String receiveMsg = String.valueOf(charset.decode(readBuffer).array());

                                System.out.println(client + receiveMsg);

                                String keyVal = null;
                                for (Map.Entry<String, SocketChannel> entry : clients.entrySet()) {
                                    if (client == entry.getValue()) {
                                        keyVal = entry.getKey();
                                        break;
                                    }
                                }
                                //将消息广播到其他客户端
                                for (Map.Entry<String, SocketChannel> entry : clients.entrySet()) {
                                    SocketChannel socketChannel = entry.getValue();
                                    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                                    writeBuffer.put((keyVal + ": " + receiveMsg).getBytes());

                                    writeBuffer.flip();
                                    socketChannel.write(writeBuffer);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                selectionKeys.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
