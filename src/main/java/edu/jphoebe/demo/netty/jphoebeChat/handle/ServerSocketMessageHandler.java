package edu.jphoebe.demo.netty.jphoebeChat.handle;

import edu.jphoebe.demo.netty.jphoebeChat.common.CustomMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.AttributeKey;

import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class ServerSocketMessageHandler extends SimpleChannelInboundHandler<CustomMessage> {

    private static ConcurrentMap<String, ChannelHandlerContext> onlineUserMap = new ConcurrentHashMap<>();
    private static ConcurrentMap<String, ChannelGroup> userGroupMap = new ConcurrentHashMap<>();
//    private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    private ConsoleInput consoleInput;

    public static class ConsoleInput extends Thread {

        private ChannelHandlerContext ctx = null;

        public ConsoleInput(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            System.out.println("你好，请在控制台输入消息内容");
            Scanner scanner = new Scanner(System.in);
            do {
                if (scanner.hasNext()) {
                    String input = scanner.nextLine();
                    ctx.channel().writeAndFlush(CustomMessage.builder()
                            .content(input)
                            .build());
                    // 这里暂停一下是防止channelRead收到的数据粘包
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            while (true);
        }
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CustomMessage msg) throws Exception {
        System.out.println("收到消息：" + msg.getContent());

        ctx.channel().attr(AttributeKey.valueOf("key")).set(msg.getKey());
        onlineUserMap.put(msg.getKey(), ctx);

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端Handler创建...");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception { // (3)
        Channel client = ctx.channel();
        System.out.println("Socket Client 离开");
        if (consoleInput != null && consoleInput.isAlive()) {
            consoleInput.interrupt();
        }
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
        System.out.println("开启服务端对话功能");
        consoleInput = new ConsoleInput(ctx);
        consoleInput.start();
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
