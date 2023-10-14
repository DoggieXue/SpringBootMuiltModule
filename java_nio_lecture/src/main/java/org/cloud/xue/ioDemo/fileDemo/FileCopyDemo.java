package org.cloud.xue.ioDemo.fileDemo;

import lombok.extern.slf4j.Slf4j;
import org.cloud.xue.NioDemoConfig;
import org.cloud.xue.common.util.IOUtil;

import java.io.*;

/**
 * @ClassName FileCopyDemo
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/11/25 下午2:35
 * @Version 1.0
 **/
@Slf4j
public class FileCopyDemo {
    public static void main(String[] args) {
        copyResourceFile();
    }

    public static void copyResourceFile() {
        String sourcePath = NioDemoConfig.FILE_RESOURCE_SRC_PATH;
        String srcDecodePath = IOUtil.getResourcePath(sourcePath);
        log.debug("srcDecodePath = " + srcDecodePath);

        String destPath = NioDemoConfig.FILE_RESOURCE_DEST_PATH;
        String destDecodePath = IOUtil.getResourcePath(destPath);
        log.debug("destDecodePath = " + destDecodePath);
        blockCopyFile(srcDecodePath, destDecodePath);
    }

    /**
     * 复制文件
     * @param srcPath
     * @param destPath
     */
    public static void blockCopyFile(String srcPath, String destPath) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File srcFile = new File(srcPath);
        File destFile = new File(destPath);
        try {
            if (!destFile.exists()) {
                destFile.createNewFile();
            }
            long startTime = System.currentTimeMillis();

            inputStream = new FileInputStream(srcFile);
            outputStream = new FileOutputStream(destFile);

            byte[] buf = new byte[1024];
            int byteRead = 0;
            while ((byteRead = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, byteRead);
                outputStream.flush();
            }

            long endTime = System.currentTimeMillis();
            log.debug("IO流复制毫秒数： " + (endTime - startTime));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(outputStream);
            IOUtil.closeQuietly(inputStream);
        }
    }
}
