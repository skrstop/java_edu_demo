package edu.jphoebe.demo.netty.jphoebeChat.server;

import edu.jphoebe.demo.netty.jphoebeChat.common.seri.json.JsonDecoder;
import edu.jphoebe.demo.netty.jphoebeChat.common.seri.json.JsonEncoder;
import edu.jphoebe.demo.netty.jphoebeChat.common.seri.text.TextDecoder;
import edu.jphoebe.demo.netty.jphoebeChat.common.seri.text.TextEncoder;
import edu.jphoebe.demo.netty.jphoebeChat.handle.ServerSocketMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author 蒋时华
 * @date 2022-07-29 16:51:31
 */
public class ServerStart {

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

                            /*** 心跳 */
                            /*
                             * IdleStateHandler: netty 提供的处理空闲状态的处理器
                             * long readerIdleTime: 多长时间没有读，就会发送一个心跳检查是否连接
                             * long writerIdleTime: 多长时间没有写，就会发送一个心跳检查是否连接
                             * long allIdleTime: 多长时间没有读写，就会发送一个心跳检查是否连接
                             */
//                            p.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
//                            p.addLast(new HeartBeatServerHandler());

                            // decode json
                            p.addLast(new JsonDecoder());
                            // decode text
                            p.addLast(new TextDecoder());
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
