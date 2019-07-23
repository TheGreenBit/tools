package com.bird.netty.server.boot;

import com.bird.netty.core.handle.HandlerRegistry;
import com.bird.netty.core.message.IMessage;
import com.bird.netty.core.netty.ChannelHandlerDelegate;
import com.bird.netty.server.help.MachineStateSendClientChooser;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;


/**
 * @Author: bird
 * @Date: 2019/6/17 15:27
 */
@Slf4j
@ChannelHandler.Sharable
public class MonitorServerHandler extends SimpleChannelInboundHandler<IMessage> {

    ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private final ChannelHandlerDelegate handlerDelegate;

    public MonitorServerHandler(HandlerRegistry handlerRegistry) {
        handlerDelegate = new ChannelHandlerDelegate(handlerRegistry);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            switch (((IdleStateEvent) evt).state()) {
                case READER_IDLE:
                    ctx.writeAndFlush(IMessage.heartbeat("Server"));
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
        MachineStateSendClientChooser.registerSendMachineInfo(ctx.channel());
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channels.remove(ctx.channel());
        MachineStateSendClientChooser.remove(ctx.channel());
        ctx.channel().close();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, IMessage message) throws Exception {
        handlerDelegate.handle(channelHandlerContext,message);
    }
}
