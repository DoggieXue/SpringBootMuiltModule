package org.cloud.xue.netty.demo.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import org.cloud.xue.common.util.Logger;
import org.cloud.xue.common.util.ThreadUtil;
import org.junit.Test;

/**
 * @ClassName OutPipeline
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/15 下午3:26
 * @Version 1.0
 **/
public class OutPipeline {
    static class SimpleOutHandlerA extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            Logger.info("出站处理器A： 被调用");
            super.write(ctx, msg, promise);
        }
    }

    static class SimpleOutHandlerB extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            Logger.info("出站处理器B： 被调用");
            super.write(ctx, msg, promise);
        }
    }

    static class SimpleOutHandlerC extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            Logger.info("出站处理器C： 被调用");
            super.write(ctx, msg, promise);
        }
    }

    @Test
    public void testPipelineOutBound () {
        ChannelInitializer initializer = new ChannelInitializer<EmbeddedChannel>() {

            @Override
            protected void initChannel(EmbeddedChannel ch) throws Exception {
                ch.pipeline().addLast(new SimpleOutHandlerA());
                ch.pipeline().addLast(new SimpleOutHandlerB());
                ch.pipeline().addLast(new SimpleOutHandlerC());
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(initializer);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(3);

        channel.writeOutbound(buf);
        ThreadUtil.sleepMilliSeconds(Integer.MAX_VALUE);
    }
}
