package org.cloudxue.ioDemo.ReactorDemo;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.NioDemoConfig;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @ClassName EchoHandler
 * @Description 负责socket连接的数据输入、业务处理、数据输出
 * @Author xuexiao
 * @Date 2021/11/28 下午9:18
 * @Version 1.0
 **/
@Slf4j
public class EchoHandler implements Runnable{

    public final SocketChannel channel;
    public final SelectionKey sk;
    public final ByteBuffer byteBuffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
    //处理器实例状态：发送和接收 一个连接对应一个处理器实例
    public static final int RECEIVING = 0, SENDING = 1;
    public int state = RECEIVING;
    public EchoHandler(Selector selector, SocketChannel socketChannel) throws IOException {
        channel = socketChannel;
        channel.configureBlocking(false);
        //仅仅获得选择键，之后再设置感兴趣的IO事件
        sk = channel.register(selector, 0);
        //将EchoHandler自身作为选择键的附件，一个连接对应一个处理器实例
        sk.attach(this);
        //注册READ就绪事件
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            if (state == RECEIVING) {
                int readLen = 0;
                while ((readLen = channel.read(byteBuffer)) > 0) {
                    log.info(new String(byteBuffer.array(), 0, readLen));
                }
                //读完毕后，准备开始写入通道
                byteBuffer.flip();
                //读完毕后，注册write就绪事件
                sk.interestOps(SelectionKey.OP_WRITE);
                //读完毕后，进入发送状态
                state = SENDING;
            }else if (state == SENDING) {
                //写入通道
                channel.write(byteBuffer);
                //写完毕后，准备开始从通道中读，缓冲区切换成写模式
                byteBuffer.clear();
                //写完毕后，注册READ就绪事件
                sk.interestOps(SelectionKey.OP_READ);
                //写完毕后，进入接收状态
                state = RECEIVING;
            }
            //处理结束了，此处不能关闭select key，需要重复使用
            //sk.cancel();
        } catch (Exception e) {
            e.printStackTrace();
            sk.cancel();
            try {
                channel.finishConnect();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
