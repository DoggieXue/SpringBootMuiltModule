package org.cloudxue.ioDemo.udpDemo;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.NioDemoConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName UDPServer
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/11/26 下午1:04
 * @Version 1.0
 **/
@Slf4j
public class UDPServer {
    public void receive() throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);
        datagramChannel.bind(new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP, NioDemoConfig.SOCKET_SERVER_PORT));
        log.debug("UDP服务端启动成功！");

        Selector selector = Selector.open();
        datagramChannel.register(selector, SelectionKey.OP_READ);

        while (selector.select() > 0) {
            Set<SelectionKey> selectorKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectorKeys.iterator();
            ByteBuffer buffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isReadable()) {
                    //读取数据报通道数据到缓冲区
                    SocketAddress address = datagramChannel.receive(buffer);
                    buffer.flip();
                    log.info(new String(buffer.array(), 0, buffer.limit()));
                    buffer.clear();
                }
            }
            iterator.remove();
        }
        selector.close();
        datagramChannel.close();
    }

    public static void main(String[] args) throws IOException{
        new UDPServer().receive();
    }
}
