package edu.jphoebe.demo.netty.chat.server.handler;

import edu.jphoebe.demo.netty.chat.processor.MsgProcessor;
import edu.jphoebe.demo.netty.chat.protocol.IMMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class SocketHandler extends SimpleChannelInboundHandler<IMMessage> {


    private MsgProcessor processor = new MsgProcessor();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMMessage msg) throws Exception {
        System.out.println("收到消息, 命令：" + msg.getCmd() + " ， 消息：" + msg.getContent());
        processor.sendMsg(ctx.channel(), msg);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端Handler创建...");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception { // (3)
        Channel client = ctx.channel();
        processor.logout(client);
        System.out.println("Socket Client:" + processor.getNickName(client) + "离开");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        super.channelInactive(ctx);
    }

    /**
     * tcp链路建立成功后调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Socket Client: 有客户端连接：" + processor.getAddress(ctx.channel()));
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
