package edu.jphoebe.demo.netty.jphoebeChat.common.seri.text;

import edu.jphoebe.demo.netty.jphoebeChat.common.CustomMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.AttributeKey;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TextDecoder extends MessageToMessageDecoder<ByteBuf> {

    private final Charset charset = StandardCharsets.UTF_8;

    /**
     * Creates a new instance with the specified character set.
     */

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        String content = msg.toString(charset);
        try {
            System.out.println("解码协议：text");
            String[] split = content.split(":");
            out.add(CustomMessage.builder()
                    .key(split[0])
                    .type(split[1])
                    .content(split[2])
                    .build());
            ctx.channel().attr(AttributeKey.valueOf("protocol")).set("text");
        } catch (Exception e) {
//            e.printStackTrace();
//            // 重置读取字节索引，因为上边已经读了（readBytes），不加这个会导致数据为空
//            msg.resetReaderIndex();
//            // 这里是复制流，复制一份，防止skipBytes跳过，导致传递的消息变成空；
//            //同时还解决引用计数器为0的异常：refCnt: 0, decrement: 1。
//            ByteBuf buffer = msg.retainedDuplicate();
//            //解决 decode() did not read anything but decoded a message的异常
//            //原因是netty不允许有字节内容不读的情况发生，所以采用下边的方法解决。
//            msg.skipBytes(msg.readableBytes());
//            out.add(buffer);
            out.add(CustomMessage.builder()
                    .key("undefined")
                    .content(content)
                    .build());
        }

    }
}
