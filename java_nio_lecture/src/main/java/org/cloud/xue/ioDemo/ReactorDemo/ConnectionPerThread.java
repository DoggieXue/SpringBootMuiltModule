package org.cloud.xue.ioDemo.ReactorDemo;

import org.cloud.xue.NioDemoConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ClassName ConnectionPerThread
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/9/1 2:25 下午
 * @Version 1.0
 **/
public class ConnectionPerThread implements Runnable{

    @Override
    public void run() {
        try{
            ServerSocket serverSocket = new ServerSocket(NioDemoConfig.SOCKET_SERVER_PORT);
            while (!Thread.interrupted()) {
                //接收到一个新连接
                Socket socket = serverSocket.accept();
                //为新连接创建一个专属的处理对象
                Handler handler = new Handler(socket);
                //创建新线程，专门负责处理新的连接
                new Thread(handler).start();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理器，将收到的内容回显
     */
    static class Handler implements Runnable {
        final Socket socket;
        Handler(Socket s) {
            this.socket = s;
        }

        @Override
        public void run() {
            while (true) {
                try{
                    byte[] input = new byte[1024];
                    //读取数据
                    socket.getInputStream().read(input);
                    //处理业务逻辑，获取查询结果
                    byte[] output = null;
                    //写入结果
                    socket.getOutputStream().write(output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
