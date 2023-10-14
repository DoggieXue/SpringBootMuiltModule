package org.cloud.xue.ioDemo.socketDemo;

import lombok.extern.slf4j.Slf4j;
import org.cloud.xue.NioDemoConfig;
import org.cloud.xue.common.util.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @ClassName NioSendClint
 * @Description 使用SocketChannel发送文件客户端
 * 客户端使用SocketChannel套接字通道，
 * 1、把文件信息发送到服务器：文件名称长度(Int)、文件名称、文件长度(Long)
 * 2、把文件内容发送到服务器：从FileChannel读取，写入Buffer缓冲区，再从Buffer缓冲区写入SocketChannel通道
 * @Author xuexiao
 * @Date 2021/11/26 上午9:11
 * @Version 1.0
 **/
@Slf4j
public class NioSendClint {

    private Charset charset = Charset.forName("UTF-8");

    /**
     * 向服务端传输文件
     */
    public void sendFile() {
        try {
            //获取文件源
            String sourcePath = NioDemoConfig.SOCKET_SEND_FILE;
            String srcPath = IOUtil.getResourcePath(sourcePath);
            log.debug("srcPath = " + srcPath);
            //获取文件目标路径
            String destFile = NioDemoConfig.SOCKET_RECEIVE_FILE;
            log.debug("destFile = " + destFile);

            File file = new File(srcPath);
            if (!file.exists()) {
                log.debug("文件不存在");
                return;
            }
            FileChannel fileChannel = new FileInputStream(file).getChannel();
            SocketChannel socketChannel = SocketChannel.open();

            socketChannel.socket().connect(new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP, NioDemoConfig.SOCKET_SERVER_PORT));
            socketChannel.configureBlocking(false);
            log.debug("Client连接服务器成功...");

            while (!socketChannel.finishConnect()) {
                log.debug("自旋等待，正在连接服务器...");
            }

            //发送文件名称和长度
            ByteBuffer buffer = sendFileNameAndLength(destFile, file, socketChannel);
            //发送文件内容
            int length = sendContent(file, fileChannel,socketChannel, buffer);
            if (length == -1) {
                IOUtil.closeQuietly(fileChannel);
                socketChannel.shutdownOutput();
                IOUtil.closeQuietly(socketChannel);
            }
            log.debug("-----------文件传输成功！-----------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送文件内容
     * @param file
     * @param fileChannel
     * @param socketChannel
     * @param buffer
     * @return
     * @throws IOException
     */
    public int sendContent (File file, FileChannel fileChannel, SocketChannel socketChannel, ByteBuffer buffer) throws IOException {
        log.debug("开始传输文件...");
        int length = 0;
        int progress = 0;
        while ((fileChannel.read(buffer)) > 0) {
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
            progress += length;
            log.debug("| " + (100 * progress) / file.length() + "% |");
        }

        return length;
    }

    /**
     * 发送文件名称、文件名称长度和文件长度
     * @param destFile 文件名称全路径
     * @param file
     * @param socketChannel
     * @return
     * @throws IOException
     */
    public ByteBuffer sendFileNameAndLength(String destFile, File file, SocketChannel socketChannel) throws IOException {
        ByteBuffer fileNameByteBuffer = charset.encode(destFile);

        ByteBuffer buffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
        //发送文件名称长度
        int fileNameLen = fileNameByteBuffer.capacity();
        buffer.putInt(fileNameLen);
        buffer.flip();
        socketChannel.write(buffer);
        buffer.clear();
        log.info("Client 文件名称长度发送完成：", fileNameLen);
        //发送文件名称
        socketChannel.write(fileNameByteBuffer);
        log.info("Client 文件名称发送完成：", destFile);

        //发送文件长度
        buffer.putLong(file.length());
        buffer.flip();
        socketChannel.write(buffer);
        buffer.clear();
        log.info("Client 文件长度发送完成：", file.length());
        return buffer;
    }

    public static void main(String[] args) {
        NioSendClint clint = new NioSendClint();
        clint.sendFile();
    }
}
