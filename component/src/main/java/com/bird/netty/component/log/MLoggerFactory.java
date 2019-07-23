package com.bird.netty.component.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: bird
 * @Date: 2019/6/21 10:00
 */
public class MLoggerFactory {

    public static Logger getLogger(String name) {
        return new LoggerWrapper(LoggerFactory.getLogger(name));
    }

    public static Logger getLogger(Class c) {
        return getLogger(c.getCanonicalName());
    }

}
