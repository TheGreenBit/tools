package com.bird.netty.server.boot;

import com.bird.netty.core.netty.CustomizeDecoder;
import com.bird.netty.core.netty.CustomizeEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author: bird
 * @Date: 2019/6/17 15:03
 */

@Slf4j
public class NettyStarter implements InitializingBean, DisposableBean {

    private final MonitorServerConfig config;

    private final ChannelHandler channelHandler;

    EventLoopGroup acceptor = new NioEventLoopGroup(1, new DefaultThreadFactory("Acceptor"));

    EventLoopGroup ioHandler = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() << 1, new DefaultThreadFactory("ioHandler"));

    DefaultEventExecutorGroup biz_executor = new DefaultEventExecutorGroup(Runtime.getRuntime().availableProcessors() << 1, new DefaultThreadFactory("biz"));


    public NettyStarter(MonitorServerConfig config, ChannelHandler monitorServerHandler) {
        this.config = config;
        this.channelHandler = monitorServerHandler;
    }


    public void start() throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(acceptor, ioHandler)
                .handler(new IdleStateHandler(6, 0, 0))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new CustomizeEncoder())
                                .addLast(new CustomizeDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                .addLast(biz_executor, channelHandler);
                    }
                });

        ChannelFuture future = bootstrap.bind(config.getPort()).syncUninterruptibly();
        if (future.isSuccess()) {
            log.info("netty server {} start successful!", future.channel().localAddress());
        } else {
            log.error("连接远程主机失败原因：{}", future.cause());
            return;
        }
        future.channel().closeFuture().sync();
    }


    @Override
    public void destroy() throws Exception {
        acceptor.shutdownGracefully();
        ioHandler.shutdownGracefully();
        biz_executor.shutdownGracefully();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }


}
