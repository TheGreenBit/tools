package com.bird.netty.core.handle;


import com.bird.netty.core.constant.HandleType;
import com.bird.netty.core.message.HandlerContext;
import com.bird.netty.core.message.TypeMessage;

/**
 * @Author: bird 心跳处理器
 * @Date: 2019/6/18 18:14
 */
public class HeartbeatHandler implements Handler<String> {
    @Override
    public TypeMessage handle(String obj, HandlerContext ext) {
        System.out.println("heartbeat:"+obj);
        return TypeMessage.EMPTY;
    }

    @Override
    public HandleType handleType() {
        return HandleType.HEARTBEAT;
    }
}
