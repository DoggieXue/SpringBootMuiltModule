package org.cloudxue.starter;

import org.cloudxue.netty.http.echo.HttpEchoServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName EchoServerBootstrapApplication
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/10/20 5:19 下午
 * @Version 1.0
 **/
@ComponentScan("org.cloudxue.netty.http")
@SpringBootApplication
public class EchoServerBootstrapApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(EchoServerBootstrapApplication.class, args);
        try {
            HttpEchoServer.startServer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
