package com.bird.netty.client.netty;

import com.bird.netty.client.ClientConfig;
import com.bird.netty.core.message.IMessage;
import com.bird.netty.core.netty.CustomizeDecoder;
import com.bird.netty.core.netty.CustomizeEncoder;
import com.google.common.net.HostAndPort;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @Author: bird
 * @Date: 2019/6/17 15:45
 */
@Slf4j
public class NettyClient implements InitializingBean {

    private static volatile Channel channel;

    public NettyClient() {
    }

    public static synchronized void init(final ChannelHandler handler) throws Exception {
        String ip = ClientConfig.getInstance().getHostAndPort();
        HostAndPort hostAndPort = HostAndPort.fromString(ip);
        NioEventLoopGroup group = new NioEventLoopGroup(4);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast("heart-beat", new IdleStateHandler(0, 3, 0))
                                    .addLast("encoder",new CustomizeEncoder())
                                    .addLast("decoder",new CustomizeDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                    .addLast(handler);
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(hostAndPort.getHost(), hostAndPort.getPort()))
                    .sync();

            if (channelFuture.isSuccess()) {
                channel = channelFuture.channel();
                //System.out.println("begin register task");
                channel.eventLoop().scheduleAtFixedRate(new SendTask(), 10, 10, TimeUnit.SECONDS);
                channel.closeFuture().sync();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            group.shutdownGracefully();
        }

    }

    public static class SendTask implements Runnable {

        @Override
        public void run() {
            //System.out.println("SendTask task run");
            DefaultMessageSender instance = DefaultMessageSender.getInstance();
            IMessage iMessage;
            if (isUsable()) {
                while ((iMessage = instance.poll()) != null) {
                    //System.out.println("polled " + iMessage);
                    NettyClient.sendImmediately(iMessage);
                }
            }
        }
    }

    static void reconnect(ChannelHandler handler) throws Exception {
        close();

        init(handler);
    }

    public static void close() {
        if (channel != null) {
            channel.close();
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
    }


    static boolean isUsable() {
        return channel != null && channel.isActive();
    }

    static void sendImmediately(IMessage o) {
        try {
            channel.writeAndFlush(o);
        } catch (Exception e) {
            log.error("发送监控数据{}失败，原因：{}", o, e);
        }
    }

    static void sendAll(Collection<IMessage> messages) {
        for (IMessage message : messages) {
            channel.write(message);
        }

        channel.flush();
    }


}
