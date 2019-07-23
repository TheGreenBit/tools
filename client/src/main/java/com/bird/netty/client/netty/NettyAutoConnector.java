package com.bird.netty.client.netty;

import com.bird.netty.client.ClientConfig;
import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: bird
 * @Date: 2019/6/20 13:44
 */
@Slf4j
public class NettyAutoConnector {

    static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private static boolean submit = false;

    public static class Checker implements Runnable {

        private final ChannelHandler handler;

        public Checker(ChannelHandler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            if (!NettyClient.isUsable()) {
                try {
                    System.out.println("need reconnect ");
                    NettyClient.reconnect(handler);
                } catch (Exception e) {
                    log.error("连接monitor server :{}失败！原因：{}", ClientConfig.getInstance().getHostAndPort(), e);
                }
            }
        }
    }

    public static void startIfRequire() {
        if (!submit) {
            synchronized (executorService) {
                if (!submit) {
                    executorService.scheduleAtFixedRate(new Checker(new ClientChannelHandler()), 0, 1, TimeUnit.MINUTES);
                    submit = true;
                }
            }
        }
    }

}
