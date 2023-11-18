package org.cloud.xue.netty.starter;

import org.cloud.xue.common.config.SystemConfig;
import org.cloud.xue.netty.websocket.WebSocketEchoServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @ClassName WebSocketEchoServerBootstrapApplication
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/10/27 11:26 上午
 * @Version 1.0
 **/
@SpringBootApplication
public class WebSocketEchoServerBootstrapApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(WebSocketEchoServerBootstrapApplication.class, args);
        try {
            WebSocketEchoServer.startServer(SystemConfig.SOCKET_SERVER_IP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
