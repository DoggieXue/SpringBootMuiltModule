package org.cloudxue.ioDemo.ReactorDemo;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.NioDemoConfig;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName MultiThreadEchoHandler
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/11/29 下午3:25
 * @Version 1.0
 **/
@Slf4j
public class MultiThreadEchoHandler implements Runnable{
    final SocketChannel channel;
    final SelectionKey sk;
    final ByteBuffer byteBuffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
    static final int RECEIVING = 0, SENDING = 1;
    int status = RECEIVING;
    //引入线程池
    static ExecutorService pool = Executors.newFixedThreadPool(4);

    MultiThreadEchoHandler(Selector selector, SocketChannel channel) throws IOException {
        this.channel = channel;
        channel.configureBlocking(false);
        //先取得选择键，再设置感兴趣的IO事件
        sk = channel.register(selector, 0);
        //将自身Handler作为sk选择键的附件，方便事件dispatch
        sk.attach(this);
        //向sk选择键设置READ就绪事件
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }
    //此run方法在IO轮询事件中被调用
    @Override
    public void run() {
//        pool.submit(() -> asyncRun());
        pool.execute(new AsyncTask());
    }

    public synchronized  void asyncRun() {
        try {
            if (status == SENDING) {
                //写入通道
                channel.write(byteBuffer);
                //写完成，准备开始从通道读，byteBuffer切换成写模式
                byteBuffer.clear();
                //写完成，注册READ就绪事件
                sk.interestOps(SelectionKey.OP_READ);
                //写完成，进入接收状态
                status = RECEIVING;
            } else if (status == RECEIVING) {
                int readLen = 0;
                while ((readLen = channel.read(byteBuffer)) > 0) {
                    log.info(new String(byteBuffer.array(), 0, readLen));
                }
                //读完成，准备开始写入通道，byteBuffer切换成读模式
                byteBuffer.flip();
                //读完成，注册WRITE就绪事件
                sk.interestOps(SelectionKey.OP_WRITE);
                //读完成，进入发送状态
                status = SENDING;
            }
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

    class AsyncTask implements Runnable {
        public void run () {
            MultiThreadEchoHandler.this.asyncRun();
        }
    }
}
