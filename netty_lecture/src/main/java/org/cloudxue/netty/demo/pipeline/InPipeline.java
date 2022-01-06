package org.cloudxue.netty.demo.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.cloudxue.common.util.Logger;
import org.cloudxue.common.util.ThreadUtil;
import org.junit.Test;

/**
 * @ClassName InPipeline
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/15 上午10:14
 * @Version 1.0
 **/
public class InPipeline {

    static class SimpleHandlerA extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Logger.info("入站处理器A： 被回调");
            super.channelRead(ctx, msg);
        }
    }

    static class SimpleHandlerB extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Logger.info("入站处理器B： 被回调");
            super.channelRead(ctx, msg);
        }
    }

    static class SimpleHandlerC extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Logger.info("入站处理器C： 被回调");
            super.channelRead(ctx, msg);
        }
    }

    @Test
    public void testPipelineInbound() {
        ChannelInitializer initializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) throws Exception {
                ch.pipeline().addLast(new SimpleHandlerA());
                ch.pipeline().addLast(new SimpleHandlerB());
                ch.pipeline().addLast(new SimpleHandlerC());
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(initializer);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        //想通道写入入站报文
        channel.writeInbound(buf);
        ThreadUtil.sleepMilliSeconds(Integer.MAX_VALUE);
    }

    static class SimpleHandlerB2 extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Logger.info("入站处理器B： 被调用");
            //不调用基类的channelRead，终止流水线执行
//            super.channelRead(ctx, msg);
        }
    }

    @Test
    public void testPipelineCutting() {
        ChannelInitializer initializer = new ChannelInitializer<EmbeddedChannel> () {
            @Override
            protected void initChannel(EmbeddedChannel ch) throws Exception {
                ch.pipeline().addLast(new SimpleHandlerA());
                ch.pipeline().addLast(new SimpleHandlerB2());
                ch.pipeline().addLast(new SimpleHandlerC());
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(initializer);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(2);

        channel.writeInbound(buf);
        ThreadUtil.sleepMilliSeconds(Integer.MAX_VALUE);
    }
}
