package com.bird.netty.server.handle;

import com.bird.netty.core.constant.HandleType;
import com.bird.netty.core.domain.LogMessage;
import com.bird.netty.core.handle.Handler;
import com.bird.netty.core.message.HandlerContext;
import com.bird.netty.core.message.TypeMessage;
import org.springframework.stereotype.Component;

/**
 * @Author: bird
 * @Date: 2019/6/21 10:14
 */
@Component
public class LoggerHandler implements Handler<LogMessage> {

    @Override
    public TypeMessage handle(LogMessage obj, HandlerContext ctx) {
        System.out.println("begin analyze receive log:" + obj);
        return TypeMessage.EMPTY;
    }

    @Override
    public HandleType handleType() {
        return HandleType.LOG_ANALYZE;
    }

}
