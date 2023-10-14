package org.cloud.xue.bufferDemo;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @ClassName ScatherAndGatherTest
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/2 下午4:46
 * @Version 1.0
 **/
public class ScatherAndGatherTest {
    public static void main(String[] args) throws  Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(8899);
        serverSocketChannel.socket().bind(address);

        int messageLen = 2 + 3 + 4;

        ByteBuffer[] buffers = new ByteBuffer[3];
        buffers[0] = ByteBuffer.allocate(2);
        buffers[1] = ByteBuffer.allocate(3);
        buffers[2] = ByteBuffer.allocate(4);

        SocketChannel channel = serverSocketChannel.accept();

        while (true) {
            int readLen = 0;
            while (readLen < messageLen) {
                long r = channel.read(buffers);
                readLen += r;
                System.out.println("readBuffer: " + readLen);

                Arrays.asList(buffers).stream().map(buffer -> "position: "+buffer.position() + ", limit: " + buffer.limit())
                        .forEach(System.out::println);
            }

            Arrays.asList(buffers).forEach(byteBuffer -> byteBuffer.flip());

            int writeLen = 0;
            while (writeLen < messageLen) {
                long w = channel.write(buffers);
                writeLen += w;
            }
            Arrays.asList(buffers).forEach(byteBuffer -> byteBuffer.clear());
            System.out.println("readBuffer: " + readLen + ", writeLen: " + writeLen + ", messageLen: " + messageLen);
        }
    }
}
