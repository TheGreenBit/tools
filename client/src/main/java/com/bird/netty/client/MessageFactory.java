package com.bird.netty.client;

import com.bird.netty.core.constant.HandleType;
import com.bird.netty.core.message.IMessage;

/**
 * @Author: bird
 * @Date: 2019/6/19 15:24
 */
public class MessageFactory {

    public static IMessage heartbeat() {
        IMessage iMessage = new IMessage();
        iMessage.setApplication(ClientConfig.getConfigApplicationName());
        iMessage.setTimestamp(System.currentTimeMillis());
        iMessage.setType(HandleType.HEARTBEAT.getMt());
        iMessage.setData("heartbeat");
        return iMessage;
    }

}
