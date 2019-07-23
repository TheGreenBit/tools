package com.bird.netty.core.netty;

import com.alibaba.fastjson.JSON;
import com.bird.netty.core.handle.HandlerRegistry;
import com.bird.netty.core.message.HandlerContext;
import com.bird.netty.core.message.IMessage;
import com.bird.netty.core.message.TypeMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;


/**
 * @Author: bird
 * @Date: 2019/6/19 11:22
 */
@Slf4j
public class ChannelHandlerDelegate  {

    protected HandlerRegistry handlerRegistry;

    public ChannelHandlerDelegate(HandlerRegistry handlerRegistry) {
        this.handlerRegistry = handlerRegistry;
    }

    public void handle(ChannelHandlerContext channelHandlerContext, IMessage iMessage) {
        HandlerRegistry.ResolvedHandler handler = handlerRegistry.getHandler(iMessage.getType());
        System.out.println("接收到的消息内容为："+iMessage);
        if (handler == null) {
            log.error("未找到类型为{}，的业务处理器", iMessage.getType());
        } else {
            try {
                Object bizParameter = null;
                if (iMessage.hasData()) {
                    if (handler.getType() != String.class) {
                        bizParameter = JSON.parseObject(iMessage.getData(), handler.getType());
                    } else {
                        bizParameter = iMessage.getData();
                    }
                }
                HandlerContext handlerContext = HandlerContext.create(iMessage, channelHandlerContext);
                TypeMessage handle = handler.handle(bizParameter, handlerContext);
                if (TypeMessage.EMPTY != handle && handle != null) {
                    channelHandlerContext.writeAndFlush(IMessage.serverRespond(handle));
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("处理项目：{},业务：{}出错，原因：{}", iMessage.getApplication(), iMessage.getType(), e);
            }
        }

    }


}
