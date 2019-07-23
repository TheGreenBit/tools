package com.bird.netty.core.netty;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bird.netty.core.message.IMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Code With Bird On 2018-09-12
 */

public class CustomizeEncoder extends MessageToByteEncoder<IMessage> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void encode(ChannelHandlerContext ctx, IMessage msg, ByteBuf out) throws Exception {
        //logger.debug("encode msg：{}",msg);
        byte[] bytes = JSONObject.toJSONBytes(msg, SerializerFeature.EMPTY);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }

}
