package com.bird.netty.client;

import com.bird.netty.core.constant.HandleType;


/**
 * @Author: bird
 * @Date: 2019/6/17 17:02
 */
public interface MessageSender {

    MessageSender sendIdle(HandleType ht, String parameter);

    void sendImmediately(HandleType ht, String message);

}
