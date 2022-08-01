package edu.jphoebe.demo.netty.jphoebeChat.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author 蒋时华
 * @date 2022-08-01 13:39:30
 */
@ChannelHandler.Sharable
public class DelimiterBasedFrameEncoder extends MessageToMessageEncoder<ByteBuf> {

    private ByteBuf delimiterBuf;

    public DelimiterBasedFrameEncoder(ByteBuf delimiter) {
        this.delimiterBuf = delimiter;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        out.add(Unpooled.wrappedBuffer(Unpooled.copiedBuffer(byteBuf, delimiterBuf)));
    }
}
