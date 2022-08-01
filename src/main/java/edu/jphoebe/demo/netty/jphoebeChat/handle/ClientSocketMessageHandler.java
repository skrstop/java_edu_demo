package edu.jphoebe.demo.netty.jphoebeChat.handle;

import edu.jphoebe.demo.netty.jphoebeChat.client.ClientStart;
import edu.jphoebe.demo.netty.jphoebeChat.common.CustomMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

@ChannelHandler.Sharable
public class ClientSocketMessageHandler extends SimpleChannelInboundHandler<CustomMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CustomMessage msg) throws Exception {
        System.out.println();
        System.out.println("收到消息：" + msg.getContent() + ", 发送人：" + msg.getKey());

        ctx.channel().attr(AttributeKey.valueOf("key")).set(msg.getKey());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端Handler创建...");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception { // (3)
        Channel client = ctx.channel();
        System.out.println("Socket Client 离开");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        super.channelInactive(ctx);
        if (!ClientStart.exit) {
            ClientStart.consoleInput.setCtx(null);
        }
    }

    /**
     * tcp链路建立成功后调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Socket Client: 有客户端连接：" + ctx.channel().toString());
        System.out.println("开启客户端对话功能");
        ClientStart.consoleInput.setCtx(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        if (!ClientStart.exit) {
            ClientStart.consoleInput.setCtx(null);
        }
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Socket Client: 与客户端断开连接:" + cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }


}
