package org.cloud.xue.ioDemo.fileDemo;

import lombok.extern.slf4j.Slf4j;
import org.cloud.xue.NioDemoConfig;
import org.cloud.xue.common.util.IOUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @ClassName FileNIOCopyDemo
 * @Description 使用Java NIO实现文件复制
 * @Author xuexiao
 * @Date 2021/11/25 下午5:19
 * @Version 1.0
 **/
@Slf4j
public class FileNIOCopyDemo {
    public static void main(String[] args) {
        nioCopyResourceFile();
    }

    public static void nioCopyResourceFile() {
        String sourcePath = NioDemoConfig.FILE_RESOURCE_SRC_PATH;
        String srcPath = IOUtil.getResourcePath(sourcePath);
        log.debug("srcPath = " + srcPath);

        String destSourcePath = NioDemoConfig.FILE_RESOURCE_DEST_PATH;
        String destPath = IOUtil.getResourcePath(destSourcePath);
        log.debug("destPath = " + destPath);
        nioCopyFile(srcPath, destPath);
    }

    public static void nioCopyFile (String srcPath, String destPath) {
        File srcFile = new File(srcPath);
        File destFile = new File(destPath);
        try {
            //如果目标文件不存在，则创建
            if (!destFile.exists()) {
                destFile.createNewFile();
            }

            long startTime = System.currentTimeMillis();
            FileInputStream fis = null;
            FileOutputStream fos = null;
            FileChannel inChannel = null;
            FileChannel outChannel = null;
            try {
                fis = new FileInputStream(srcFile);
                fos = new FileOutputStream(destFile);
                inChannel = fis.getChannel();
                outChannel = fos.getChannel();

                int length = -1;
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                //从通道读取数据，写入到缓冲区，
                while ((length = inChannel.read(buffer)) != -1) {
                    //将缓冲区变成读模式
                    buffer.flip();

                    int outlength = 0;
                    //从缓冲区读取数据，写入通道
                    while ((outlength = outChannel.write(buffer)) != 0) {
                        log.info("写入字节数： " + outlength);
                    }
                    //清空缓冲区，变成可写模式
                    buffer.clear();
                }
                //强制刷新磁盘
                outChannel.force(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                IOUtil.closeQuietly(outChannel);
                IOUtil.closeQuietly(fos);
                IOUtil.closeQuietly(inChannel);
                IOUtil.closeQuietly(fis);
            }
            long endTime = System.currentTimeMillis();
            log.info("base NIO 文件复制毫秒数： " + (endTime - startTime));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
