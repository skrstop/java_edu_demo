package edu.jphoebe.demo.netty.jphoebeChat.client;

import edu.jphoebe.demo.netty.jphoebeChat.common.CustomMessage;
import edu.jphoebe.demo.netty.jphoebeChat.common.seri.json.JsonDecoder;
import edu.jphoebe.demo.netty.jphoebeChat.common.seri.json.JsonEncoder;
import edu.jphoebe.demo.netty.jphoebeChat.common.seri.text.TextDecoder;
import edu.jphoebe.demo.netty.jphoebeChat.common.seri.text.TextEncoder;
import edu.jphoebe.demo.netty.jphoebeChat.handle.ClientReconnectHandler;
import edu.jphoebe.demo.netty.jphoebeChat.handle.ClientSocketMessageHandler;
import edu.jphoebe.demo.netty.jphoebeChat.handle.DelimiterBasedFrameEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.base64.Base64Decoder;
import io.netty.handler.codec.base64.Base64Encoder;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author 蒋时华
 * @date 2022-08-01 09:28:29
 */
public class ClientStart {

    public static ConsoleInput consoleInput;
    public static boolean exit = false;

    public static void main(String[] args) {
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);
        EventLoopGroup handleGroup = new DefaultEventLoopGroup(8);
        try {
            Bootstrap b = new Bootstrap();
            b.remoteAddress("127.0.0.1", 18181);
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    /*** 自动重连 */
                    p.addLast(new ClientReconnectHandler(b));

                    /*** 心跳 */
                    /*
                     * IdleStateHandler: netty 提供的处理空闲状态的处理器
                     * long readerIdleTime: 多长时间没有读，就会发送一个心跳检查是否连接
                     * long writerIdleTime: 多长时间没有写，就会发送一个心跳检查是否连接
                     * long allIdleTime: 多长时间没有读写，就会发送一个心跳检查是否连接
                     */
//                            p.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
//                            p.addLast(new HeartBeatServerHandler());

                    ByteBuf byteBuf = Unpooled.wrappedBuffer("&&&&".getBytes(StandardCharsets.UTF_8));

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
                    p.addLast(handleGroup, new ClientSocketMessageHandler());


                    // http
                    // websocket
                }
            });
            ChannelFuture f = b.connect();
//                    .addListener(new ClientReconnectHandler(b));
//                    .sync();
            consoleInput = new ConsoleInput(b);
            consoleInput.start();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
//            workerGroup.shutdownGracefully();
//            handleGroup.shutdownGracefully();
        }


    }


    public static class ConsoleInput extends Thread {

        private AtomicReference<ChannelHandlerContext> ctx = new AtomicReference<>(null);
        private Bootstrap b;

        public ConsoleInput(Bootstrap b) {
            this.b = b;
        }

        public void setCtx(ChannelHandlerContext ctx) {
            System.out.println("更换连接通道");
            this.ctx.set(ctx);
        }

        @Override
        public void run() {
            System.out.println("你好，请在控制台输入消息内容");
            Scanner scanner = new Scanner(System.in);
            do {
                if (scanner.hasNext()) {
                    String input = scanner.nextLine();
                    if ("exit".equals(input)) {
                        System.out.println("退出");
                        exit = true;
                        ctx.get().channel().close();
                        continue;
                    } else if ("reconnect".equals(input) && b != null) {
                        exit = false;
                        if (this.ctx.get().channel().isActive()) {
                            System.out.println("当前连接正常，不需要重连");
                            continue;
                        }
                        EventLoop eventLoop = this.ctx.get().channel().eventLoop();
                        eventLoop.schedule(() -> b.connect().addListener(new ClientReconnectHandler(b)), 3, TimeUnit.SECONDS);
                        continue;
                    }
                    if (ctx != null
                            && ctx.get() != null
                            && ctx.get().channel().isActive()) {
                        ctx.get().channel().writeAndFlush(CustomMessage.builder()
                                .content(input)
                                .build());
                    }
                }
            }
            while (true);
        }
    }


}
