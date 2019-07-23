package com.bird.netty.core.handle;


import com.bird.netty.core.constant.HandleType;
import com.bird.netty.core.message.HandlerContext;
import com.bird.netty.core.message.TypeMessage;

/**
 * @Author: bird 直接丢弃处理的消息
 * @Date: 2019/6/19 9:52
 */
public class DiscardHandler implements Handler<String> {
    @Override
    public TypeMessage handle(String obj, HandlerContext ext) {
        System.out.println("discard message " + obj);
        //
        return TypeMessage.EMPTY;
    }

    @Override
    public HandleType handleType() {
        return HandleType.DISCARD;
    }
}
