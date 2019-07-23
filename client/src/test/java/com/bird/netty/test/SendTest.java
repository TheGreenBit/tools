package com.bird.netty.test;


import com.bird.netty.client.netty.DefaultMessageSender;
import com.bird.netty.core.constant.HandleType;

/**
 * @Author: bird
 * @Date: 2019/6/17 16:08
 */
public class SendTest {

    public static void main(String[] args) throws Exception {
        DefaultMessageSender.send(HandleType.DISCARD,"hello bird ，welocome");
    }

}
