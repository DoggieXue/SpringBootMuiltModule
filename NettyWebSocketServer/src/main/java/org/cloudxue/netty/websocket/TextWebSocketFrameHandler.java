package org.cloudxue.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.common.util.DateUtil;

/**
 * @ClassName TextWebSocketFrameHandler
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/10/28 9:28 上午
 * @Version 1.0
 **/
@Slf4j
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {

        if (msg instanceof TextWebSocketFrame) {
            String request = ((TextWebSocketFrame) msg).text();
            log.debug("服务端收到请求： " + request);

            String echoContent = DateUtil.getTime() +  " 服务端 echo: " +  request;
            TextWebSocketFrame echoFrame = new TextWebSocketFrame(echoContent);
            //发送回显字符串
            ctx.channel().writeAndFlush(echoFrame);
        } else {
            log.debug("本系统仅支持文本消息，暂不支持二进制消息");
            String message = "unsupported frame type: " + msg.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //握手成功，升级为WebSocket协议
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            //握手成功，移除WebPageHandler
            ctx.pipeline().remove(WebPageHandler.class);
            log.debug("WebSocket HandshakeComplete 我收成功");
            log.debug("新的WS客户端加入，通道为： " + ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
