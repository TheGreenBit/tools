package com.bird.netty.core.handle;


import com.bird.netty.core.constant.HandleType;
import com.bird.netty.core.message.HandlerContext;
import com.bird.netty.core.message.TypeMessage;

/**
 * @Author: bird
 * @Date: 2019/6/18 10:59
 */
public interface Handler<T> {

    TypeMessage handle(T obj, HandlerContext ctx);

    HandleType handleType();


}
