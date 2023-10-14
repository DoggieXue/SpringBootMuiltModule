package org.cloud.xue.ioDemo.nioDiscardDemo;

import lombok.extern.slf4j.Slf4j;
import org.cloud.xue.NioDemoConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @ClassName NioDiscardClient
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/11/28 下午7:04
 * @Version 1.0
 **/
@Slf4j
public class NioDiscardClient {
    public static void send() throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP, NioDemoConfig.SOCKET_SERVER_PORT);
        SocketChannel socketChannel = SocketChannel.open(serverAddress);
        socketChannel.configureBlocking(false);
        while (!socketChannel.finishConnect()) {
            log.info("连接中...");
        }
        log.info("客户端连接成功");
        ByteBuffer buffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
        buffer.put("hello world".getBytes());
        buffer.flip();
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()) {
//            String next = scanner.next();
//            if ("quit".equals(next)) {
//                break;
//            } else {
//                buffer.put((DateUtil.getNow() + ">>>>>" + next).getBytes());
//                buffer.flip();
//                //发送到服务器
//                socketChannel.write(buffer);
//                buffer.clear();
//            }
//        }
        socketChannel.shutdownOutput();
        socketChannel.close();
    }

    public static void main(String[] args) throws IOException{
        send();
    }
}
