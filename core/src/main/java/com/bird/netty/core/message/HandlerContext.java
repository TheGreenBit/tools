package com.bird.netty.core.message;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;


/**
 * @Author: bird
 * @Date: 2019/6/19 13:44
 */
@Data
public class HandlerContext {

    private long timestamp;

    private ChannelHandlerContext ctx;

    //原始消息
    private String origin;

    private String application;

    private int type;


    public static HandlerContext create(IMessage message, ChannelHandlerContext ctx) {
        HandlerContext handlerContext = new HandlerContext();
        handlerContext.setCtx(ctx);
        handlerContext.setApplication(message.getApplication());
        handlerContext.setOrigin(message.getData());

        return handlerContext;
    }

}
