package edu.jphoebe.demo.netty.jphoebeChat.handle;

import cn.auntec.framework.components.util.value.format.TextFormatUtil;
import edu.jphoebe.demo.netty.jphoebeChat.client.ClientStart;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@ChannelHandler.Sharable
public class ClientReconnectHandler extends ChannelInboundHandlerAdapter implements GenericFutureListener<ChannelFuture> {

    private final Bootstrap b;
    private int retryCount = 3;
    private volatile static boolean reconnect = true;
    private volatile static AtomicInteger reconnectCount = new AtomicInteger(0);
    private int delayTimeBase = 3;
    private int delayTimeMax = 9;

    public ClientReconnectHandler(Bootstrap b) {
        this.b = b;
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        if (reconnect && !ClientStart.exit) {
            synchronized (this) {
                if (reconnect && !ClientStart.exit) {
                    System.out.println("触发自动重连");
                    EventLoop eventLoop = ctx.channel().eventLoop();
                    eventLoop.schedule(() -> b.connect().addListener(this), 3, TimeUnit.SECONDS);
                    reconnect = false;
                }
            }
        }
    }

    @Override
    public void operationComplete(ChannelFuture f) throws Exception {
        final EventLoop eventLoop = f.channel().eventLoop();
        if (!f.isSuccess()) {
            int count = reconnectCount.incrementAndGet();
            int delayTime = delayTimeBase * count;
            if (delayTime > delayTimeMax) {
                delayTime = delayTimeMax;
            }
            System.out.println("连接失败! 在" + delayTime + "s之后准备尝试重连! ");
            eventLoop.schedule(() -> b.connect().addListener(this), delayTime, TimeUnit.SECONDS);
        } else {
            reconnect = true;
            System.out.println(TextFormatUtil.formatString("连接成功: localAddress => {} remoteAddress => {}", f.channel().localAddress(), f.channel().remoteAddress()));
        }


    }
}
