package org.cloudxue.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.common.http.HttpProtocolHelper;
import org.cloudxue.common.util.IOUtil;

/**
 * @ClassName WebPageHandler
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/10/27 2:30 下午
 * @Version 1.0
 **/
@Slf4j
public class WebPageHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        /**
         * HTTP请求格式不正确
         */
        if (!msg.decoderResult().isSuccess()) {
            HttpProtocolHelper.sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        /**
         * 只允许GET请求
         */
        if (!HttpMethod.GET.equals(msg.method())) {
            HttpProtocolHelper.sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }

        HttpProtocolHelper.cacheHttpProtocol(ctx, msg);

        String webContent = IOUtil.loadResourceFile("index.html");

        /**
         * 发送WEB操作页面
         */
        HttpProtocolHelper.sendWebPage(ctx, webContent);
    }
}
