package org.cloudxue.ioDemo.socketDemo;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.NioDemoConfig;
import org.cloudxue.common.util.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @ClassName NioReceiveServer
 * @Description 使用SocketChannel在服务器端接收文件
 * @Author xuexiao
 * @Date 2021/11/26 上午10:21
 * @Version 1.0
 **/
@Slf4j
public class NioReceiveServer {
    private Charset charset = Charset.forName("UTF-8");

    /**
     * 服务器端保存的客户端对象，对应一个客户端文件
     */
    static class Client {
        //文件名称
        String fileName;
        //文件长度
        long fileLength;
        //开始传输时间
        long startTime;
        //客户端地址
        InetSocketAddress remoteAddress;
        //输出的文件通道
        FileChannel outChannel;
        //接收长度
        long receiveLength;

        public boolean isFinished() {
            return receiveLength >= fileLength;
        }
    }

    /**
     * 读写缓冲区
     */
    private ByteBuffer buffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);

    /**
     * 使用Map保存每个客户端传输，当OP_READ通道可读时，根据channel找到对应的对象
     */
    Map<SelectableChannel, Client> clientMap = new HashMap<>();

    public void startServer () throws IOException {
        //1、获取Selector选择器
        Selector selector = Selector.open();
        //2、获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //3、设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //4、绑定连接
        InetSocketAddress address = new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_PORT);
        serverSocketChannel.bind(address);
        //5、将通道注册到选择器上，并注册IO事件："接收新连接"
        serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
        log.debug("serverSocketChannel is listening...");
        //6、轮询感兴趣的IO就绪事件
        while (selector.select() > 0) {
            if (null == selector.selectedKeys()) continue;
            //7、获取选择键集合
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            if (iterator.hasNext()) {
                //8、获取单个选择键，并处理
                SelectionKey key = iterator.next();
                if (null == key) continue;
                //9、判断key是具体的什么事件，是否为新连接事件
                if (key.isAcceptable()) {
                    //10、若接收事件是"新连接"事件，就获取客户端新连接
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = server.accept();
                    if (socketChannel == null) continue;
                    //11、客户端新连接，切换为非阻塞模式
                    socketChannel.configureBlocking(false);
                    //12、将客户端新连接通道注册到Selector选择器上
                    SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
                    //业务处理
                    Client client = new Client();
                    client.remoteAddress = (InetSocketAddress) socketChannel.getRemoteAddress();
                    clientMap.put(socketChannel, client);
                    log.debug(socketChannel.getRemoteAddress() + "连接成功...");
                } else if (key.isReadable()) {
                    processData(key);
                }
                //NIO的特点只会累加，已选择的键集合不会删除
                //若不删除，下一次又会
                iterator.remove();
            }
        }
    }

    /**
     * 处理客户端传输过来的数据
     * @param key
     * @throws IOException
     */
    private void processData(SelectionKey key) throws IOException {
        Client client = clientMap.get(key.channel());

        SocketChannel socketChannel = (SocketChannel) key.channel();
        int readNum = 0;
        try {
            buffer.clear();
            while ((readNum = socketChannel.read(buffer)) > 0) {
                buffer.flip();
                //客户端发送过来的是文件名称
                if (null == client.fileName) {
                    if (buffer.limit() < 4) {
                        continue;
                    }
                    int fileNameLen = buffer.getInt();
                    byte[] fileNameBytes = new byte[fileNameLen];
                    buffer.get(fileNameBytes);

                    String fileName = new String(fileNameBytes, charset);

                    File directory = new File(NioDemoConfig.SOCKET_RECEIVE_PATH);
                    if (!directory.exists()) {
                        directory.mkdir();
                    }
                    log.debug("NIO 传输目标目录：", directory);

                    client.fileName = fileName;
                    String fullName = directory.getAbsolutePath() + File.pathSeparator + fileName;
                    log.debug("NIO 传输目标文件：", fullName);

                    File outFile = new File(fullName);
                    if (!outFile.exists()) {
                        outFile.createNewFile();
                    }

                    FileChannel fileChannel = new FileOutputStream(outFile).getChannel();
                    client.outChannel = fileChannel;

                    if (buffer.limit() < 8) {
                        continue;
                    }

                    long fileLength = buffer.getLong();
                    client.fileLength = fileLength;
                    client.startTime = System.currentTimeMillis();
                    log.debug("NIO 传输开始...");

                    client.receiveLength += buffer.capacity();
                    if (buffer.limit() > 0) {
                        //写入文件
                        client.outChannel.write(buffer);
                    }
                    if (client.isFinished()) {
                        finished(key, client);
                    }
                }else { //客户端发送过来的文件内容
                    client.receiveLength += buffer.capacity();
                    client.outChannel.write(buffer);
                    if (client.isFinished()) {
                        finished(key, client);
                    }
                }
                buffer.clear();
            }
            key.cancel();
        } catch (Exception e) {
            key.cancel();
            e.printStackTrace();
            return;
        }
        if (readNum == -1) {
            finished(key, client);
        }
    }

    private void finished(SelectionKey key, Client client) {
        IOUtil.closeQuietly(client.outChannel);
        log.info("传输完毕...");
        key.cancel();
        log.debug("文件接收成功，File Name: " + client.fileName);
        log.debug("" + IOUtil.getFormatFileSize(client.fileLength));
        long endTime = System.currentTimeMillis();
        log.debug("NIO IO传输毫秒数： " + (endTime - client.startTime));
    }

    public static void main(String[] args) throws IOException {
        NioReceiveServer server = new NioReceiveServer();
        server.startServer();
    }
}
