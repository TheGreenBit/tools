package com.bird.netty.component.log;

import ch.qos.logback.core.ConsoleAppender;
import com.alibaba.fastjson.JSONObject;
import com.bird.netty.client.netty.DefaultMessageSender;
import com.bird.netty.core.constant.HandleType;
import com.bird.netty.core.domain.LogMessage;

/**
 * @Author: bird
 * @Date: 2019/6/21 16:36
 */
public class CustomizeLogbackConsoleAppender extends ConsoleAppender {

    @Override
    protected void append(Object eventObject) {
        if (eventObject instanceof ch.qos.logback.classic.spi.LoggingEvent) {
            ch.qos.logback.classic.spi.LoggingEvent event = (ch.qos.logback.classic.spi.LoggingEvent) eventObject;
            LogMessage logMessage = new LogMessage();
            logMessage.setClassName(event.getLoggerName());
            logMessage.setThreadName(event.getThreadName());
            logMessage.setLevel(event.getLevel().toInt());
            logMessage.setTimestamp(event.getTimeStamp());
            DefaultMessageSender.send(HandleType.LOG_ANALYZE,JSONObject.toJSONString(logMessage));
        }
        super.append(eventObject);
    }
}
