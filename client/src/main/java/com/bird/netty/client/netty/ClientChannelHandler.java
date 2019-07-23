package com.bird.netty.client.netty;

import com.bird.netty.client.MessageFactory;
import com.bird.netty.client.handler.MachineStateSendHandler;
import com.bird.netty.core.handle.HandlerRegistry;
import com.bird.netty.core.message.IMessage;
import com.bird.netty.core.netty.ChannelHandlerDelegate;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;


/**
 * @Author: bird
 * @Date: 2019/6/18 14:25
 */
@ChannelHandler.Sharable
public class ClientChannelHandler extends SimpleChannelInboundHandler<IMessage> {

    private final ChannelHandlerDelegate handlerDelegate ;

    public ClientChannelHandler() {
        HandlerRegistry handlerRegistry = new HandlerRegistry();
        handlerRegistry.registry(new MachineStateSendHandler());
        handlerDelegate = new ChannelHandlerDelegate(handlerRegistry);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, IMessage message) throws Exception {
        handlerDelegate.handle(channelHandlerContext,message);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                    ctx.writeAndFlush(MessageFactory.heartbeat());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("channel active");
    }





    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
