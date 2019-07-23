package com.bird.netty.component.log;

import ch.qos.logback.core.rolling.RollingFileAppender;

/**
 * @Author: bird
 * @Date: 2019/6/21 16:31
 */
@Deprecated
public class CustomizeLogbackAppender extends RollingFileAppender {

    public CustomizeLogbackAppender() {
    }

    @Override
    protected void append(Object eventObject) {
        System.out.println("++++++++++++++" + eventObject);
        super.append(eventObject);

    }
}
