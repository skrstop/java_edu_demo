package edu.jphoebe.demo.netty.jphoebeChat.server;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import edu.jphoebe.demo.netty.jphoebeChat.common.CustomMessage;
import edu.jphoebe.demo.netty.jphoebeChat.common.seri.json.JsonDecoder;
import edu.jphoebe.demo.netty.jphoebeChat.common.seri.json.JsonEncoder;
import edu.jphoebe.demo.netty.jphoebeChat.common.seri.text.TextDecoder;
import edu.jphoebe.demo.netty.jphoebeChat.common.seri.text.TextEncoder;
import edu.jphoebe.demo.netty.jphoebeChat.handle.DelimiterBasedFrameEncoder;
import edu.jphoebe.demo.netty.jphoebeChat.handle.ServerSocketMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.base64.Base64Decoder;
import io.netty.handler.codec.base64.Base64Encoder;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author 蒋时华
 * @date 2022-07-29 16:51:31
 */
public class ServerStart {

    public static ConcurrentMap<String, ChannelHandlerContext> onlineUserMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<ChannelHandlerContext, String> onlineUserMap2 = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup subGroup = new NioEventLoopGroup(4);
        EventLoopGroup handleGroup = new DefaultEventLoopGroup(8);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            ChannelFuture channelFuture = serverBootstrap.group(bossGroup, subGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            ByteBuf byteBuf = Unpooled.wrappedBuffer("&&&&".getBytes(StandardCharsets.UTF_8));


                            /*** 心跳 */
                            /*
                             * IdleStateHandler: netty 提供的处理空闲状态的处理器
                             * long readerIdleTime: 多长时间没有读，就会发送一个心跳检查是否连接
                             * long writerIdleTime: 多长时间没有写，就会发送一个心跳检查是否连接
                             * long allIdleTime: 多长时间没有读写，就会发送一个心跳检查是否连接
                             */
//                            p.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
//                            p.addLast(new HeartBeatServerHandler());

                            // 粘包和拆包
                            p.addLast(new DelimiterBasedFrameDecoder(5 * 1024
                                    , true
                                    , false
                                    , byteBuf));
                            // decode base64
                            p.addLast(new Base64Decoder());
                            // decode json
                            p.addLast(new JsonDecoder());
                            // decode text
                            p.addLast(new TextDecoder());

                            // 粘包和拆包
                            p.addLast(new DelimiterBasedFrameEncoder(byteBuf));
                            // encode base64
                            p.addLast(new Base64Encoder());
                            // encode text
                            p.addLast(new TextEncoder());
                            // encode json
                            p.addLast(new JsonEncoder());

                            // socket
                            p.addLast(handleGroup, new ServerSocketMessageHandler());


                            // http
                            // websocket
                        }
                    })
                    .bind(18181)
                    .sync();
            System.out.println("服务已启动,监听端口: " + 18181);

            new Thread(() -> {
                System.out.println("开启服务端对话功能");
                System.out.println("你好，请在控制台输入消息内容");
                Scanner scanner = new Scanner(System.in);
                do {
                    if (!scanner.hasNext()) {
                        continue;
                    }
                    String input = scanner.nextLine();
                    if ("users".equals(input)) {
                        System.out.println("当前在线用户：" + onlineUserMap.size());
                        continue;
                    }
                    String[] split = input.split(":");
                    String key = "default";
                    if (split.length >= 2) {
                        key = split[0];
                    }
                    input = StrUtil.removePrefix(input, key + ":");
                    ChannelHandlerContext channelHandlerContext = ServerStart.onlineUserMap.get(key);
                    if (ObjectUtil.isNotNull(channelHandlerContext)) {
                        channelHandlerContext.channel().writeAndFlush(CustomMessage.builder()
                                .key("server")
                                .content(input)
                                .type("chat")
                                .build());
                    } else {
                        System.out.println("没有找到用户: " + key);
                    }
                }
                while (true);
            }).start();

            channelFuture.channel()
                    .closeFuture()
                    .sync();
        } finally {
            bossGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
            handleGroup.shutdownGracefully();
        }

    }

}
