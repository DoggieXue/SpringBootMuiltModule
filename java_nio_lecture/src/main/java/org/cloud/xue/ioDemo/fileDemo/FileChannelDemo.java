package org.cloud.xue.ioDemo.fileDemo;

import org.cloud.xue.NioDemoConfig;
import org.cloud.xue.common.util.IOUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @ClassName FileChannelDemo
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/1 上午11:12
 * @Version 1.0
 **/
public class FileChannelDemo {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(IOUtil.getResourcePath(NioDemoConfig.FILE_RESOURCE_SRC_PATH));
        FileOutputStream fileOutputStream = new FileOutputStream(IOUtil.getResourcePath(NioDemoConfig.FILE_RESOURCE_DEST_PATH));

        FileChannel inputChannel = fileInputStream.getChannel();
        FileChannel outputChannel = fileOutputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        ByteBuffer bytebuffer = ByteBuffer.allocateDirect(1024);
        while (true) {
            //若该行代码注释掉会发生什么？
            buffer.clear();
            int readLen = inputChannel.read(buffer);
            if (readLen == -1) {
                break;
            }
            System.out.println("read: " + readLen);
            buffer.flip();

            outputChannel.write(buffer);
        }
        outputChannel.close();
        inputChannel.close();
    }
}
