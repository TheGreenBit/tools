package com.bird.netty.component.test;


import com.bird.netty.client.netty.DefaultMessageSender;
import com.bird.netty.core.constant.HandleType;

/**
 * @Author: bird
 * @Date: 2019/6/21 16:51
 */
public class LoggerAppenderTest {

    public static void main(String[] args) {
        DefaultMessageSender.send(HandleType.DISCARD, "discard this message！");
    }
}
