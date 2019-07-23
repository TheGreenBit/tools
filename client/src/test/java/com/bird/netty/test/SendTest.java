package com.bird.netty.test;


import com.alibaba.fastjson.JSON;
import com.bird.netty.client.netty.DefaultMessageSender;
import com.bird.netty.core.constant.HandleType;
import com.bird.netty.core.domain.LogMessage;

/**
 * @Author: bird
 * @Date: 2019/6/17 16:08
 */
public class SendTest {

    public static void main(String[] args) throws Exception {

        LogMessage logMessage = new LogMessage();
        logMessage.setClassName("com.bird.xxx");
        logMessage.setMessage("it is error message cause!");
        logMessage.setLevel(1);
        DefaultMessageSender.send(HandleType.LOG_ANALYZE, JSON.toJSONString(logMessage));
    }

}
