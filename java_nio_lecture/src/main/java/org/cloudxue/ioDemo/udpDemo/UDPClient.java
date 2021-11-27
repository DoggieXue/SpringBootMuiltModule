package org.cloudxue.ioDemo.udpDemo;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.NioDemoConfig;
import org.cloudxue.common.util.DateUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

/**
 * @ClassName UDPClient
 * @Description 获取用户的输入数据，通过DatagramChannel数据报通道，将数据发送到远程服务器
 * UDP协议不是面向连接的协议，只需要知道服务器的IP和PORT就可以直接向对方发送数据
 * @Author xuexiao
 * @Date 2021/11/26 下午1:02
 * @Version 1.0
 **/
@Slf4j
public class UDPClient {
    public void send() throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);

        ByteBuffer buffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
        Scanner scanner = new Scanner(System.in);
        log.debug("UDP 客户端启动成功！");
        log.debug("请输入发送内容");
        while (scanner.hasNext()) {
            String next = scanner.next();
            buffer.put((DateUtil.getNow() + ">>" + next).getBytes());
            buffer.flip();
            datagramChannel.send(buffer, new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP, NioDemoConfig.SOCKET_SERVER_PORT));
            buffer.clear();
        }
        datagramChannel.close();
    }

    public static void main(String[] args) throws IOException{
        new UDPClient().send();
    }
}
