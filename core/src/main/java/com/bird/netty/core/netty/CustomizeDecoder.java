package com.bird.netty.core.netty;

import com.alibaba.fastjson.JSONObject;
import com.bird.netty.core.message.IMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * The Code With Bird On 2018-09-11
 */
public class CustomizeDecoder extends LengthFieldBasedFrameDecoder {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public CustomizeDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    public CustomizeDecoder(
            int maxFrameLength,
            int lengthFieldOffset,
            int lengthFieldLength,
            int lengthAdjustment,
            int initialBytesToStrip) {
        super(
                maxFrameLength,
                lengthFieldOffset,
                lengthFieldLength,
                lengthAdjustment,
                initialBytesToStrip);
    }

    public CustomizeDecoder(
            int maxFrameLength,
            int lengthFieldOffset,
            int lengthFieldLength,
            int lengthAdjustment,
            int initialBytesToStrip,
            boolean failFast) {
        super(
                maxFrameLength,
                lengthFieldOffset,
                lengthFieldLength,
                lengthAdjustment,
                initialBytesToStrip,
                failFast);
    }

    public CustomizeDecoder(
            ByteOrder byteOrder,
            int maxFrameLength,
            int lengthFieldOffset,
            int lengthFieldLength,
            int lengthAdjustment,
            int initialBytesToStrip,
            boolean failFast) {
        super(
                byteOrder,
                maxFrameLength,
                lengthFieldOffset,
                lengthFieldLength,
                lengthAdjustment,
                initialBytesToStrip,
                failFast);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf buffer = (ByteBuf) super.decode(ctx, in);
        if (buffer != null) {
            try {
                String s = buffer.toString(StandardCharsets.UTF_8);
                //System.out.println(s);
                Object parse = JSONObject.parseObject(s, IMessage.class);
                return parse;
            } catch (Exception e) {
                logger.error("消息解码出错！原因：{}", e);
                // throw new UnsupportedOperationException(e.getMessage());
            } finally {
                buffer.release();
            }
        }
        return null;
    }
}
