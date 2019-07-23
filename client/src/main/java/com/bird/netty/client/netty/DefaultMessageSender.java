package com.bird.netty.client.netty;

import com.bird.netty.client.ClientConfig;
import com.bird.netty.client.MessageSender;
import com.bird.netty.core.constant.HandleType;
import com.bird.netty.core.message.IMessage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author: bird
 * @Date: 2019/6/18 13:53
 */
public class DefaultMessageSender implements MessageSender {

    private final BlockingQueue<IMessage> MESSAGES;

    static final DefaultMessageSender sender = new DefaultMessageSender();

    private DefaultMessageSender() {
        MESSAGES = new LinkedBlockingQueue<>(ClientConfig.queenBuffer());
    }

    public static DefaultMessageSender getInstance() {
        NettyAutoConnector.startIfRequire();
        return sender;
    }

    public static void send(HandleType ht, String data) {
        getInstance().sendIdle(ht,data);
    }


    @Override
    public MessageSender sendIdle(HandleType ht, String data) {
        IMessage of = IMessage.of(ht, data);
        of.setApplication(ClientConfig.getConfigApplicationName());
        MESSAGES.add(of);
        return this;
    }

    @Override
    public void sendImmediately(HandleType ht, String data) {
        if (NettyClient.isUsable()) {
            IMessage of = IMessage.of(ht, data);
            of.setApplication(ClientConfig.getConfigApplicationName());
            NettyClient.sendImmediately(of);
        }
    }

    IMessage poll() {
        return MESSAGES.poll();
    }

}
