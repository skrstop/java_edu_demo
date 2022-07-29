package edu.jphoebe.demo.netty.jphoebeChat.common.seri.json;

import cn.hutool.json.JSONUtil;
import edu.jphoebe.demo.netty.jphoebeChat.common.CustomMessage;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonEncoder extends MessageToMessageEncoder<CustomMessage> {

    private final Charset charset = StandardCharsets.UTF_8;

    @Override
    protected void encode(ChannelHandlerContext ctx, CustomMessage msg, List<Object> out) throws Exception {
        if (msg == null) {
            return;
        }
        Attribute<Object> protocol = ctx.channel().attr(AttributeKey.valueOf("protocol"));
        if (protocol.get() == null || !protocol.get().equals("json")) {
            out.add(msg);
            return;
        }
        System.out.println("编码协议：json");
        String message = JSONUtil.toJsonStr(msg);
        System.out.println("发送消息：" + message);
        out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(message), charset));
    }
}
