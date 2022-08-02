package edu.jphoebe.demo.netty.jphoebeChat.handle;

import edu.jphoebe.demo.netty.jphoebeChat.common.CustomMessage;
import edu.jphoebe.demo.netty.jphoebeChat.server.ServerStart;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


@ChannelHandler.Sharable
public class ServerSocketMessageHandler extends SimpleChannelInboundHandler<CustomMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CustomMessage msg) throws Exception {
        System.out.println();
        if ("login".equals(msg.getType())
                && !ServerStart.onlineUserMap.containsKey(msg.getKey())) {
            ServerStart.onlineUserMap.put(msg.getKey(), ctx);
            ServerStart.onlineUserMap2.put(ctx, msg.getKey());
            System.out.println("新用户登录：" + msg.getKey());
            System.out.println("当前在线人数：" + ServerStart.onlineUserMap.size());
        } else if (ServerStart.onlineUserMap.containsKey(msg.getKey())) {
            System.out.println("收到消息：" + msg.getContent() + ", 发送人：" + msg.getKey());
        } else {
            System.out.println("未知消息：" + msg.getContent() + ", 未知发送人：" + msg.getKey());
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端Handler创建...");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception { // (3)
        System.out.println("Socket Client 离开");
        String key = ServerStart.onlineUserMap2.get(ctx);
        ServerStart.onlineUserMap2.remove(ctx);
        if (key != null) {
            ServerStart.onlineUserMap.remove(key);
            System.out.println("用户离开：" + key);
        }
        System.out.println("当前在线人数：" + ServerStart.onlineUserMap.size());
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
        System.out.println("Socket Client: 有客户端连接：" + ctx.channel().toString());
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
