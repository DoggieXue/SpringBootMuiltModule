package org.cloud.xue.netty.demo.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * @ClassName OutHandlerDemoTester
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/7 下午5:04
 * @Version 1.0
 **/
public class OutHandlerDemoTester {
    @Test
    public void testOutHandlerLifeCircle () {
        final OutHandlerDemo handlerDemo = new OutHandlerDemo();

        ChannelInitializer initializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel channel) throws Exception {
                channel.pipeline().addLast(handlerDemo);
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(initializer);

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);

        ChannelFuture future = channel.pipeline().writeAndFlush(buf);
        future.addListener(future1 -> {
            if (future.isSuccess()) {
                System.out.println("write is finished");
            }
            channel.close();
        });

//        try {
//            Thread.sleep(Integer.MAX_VALUE);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
