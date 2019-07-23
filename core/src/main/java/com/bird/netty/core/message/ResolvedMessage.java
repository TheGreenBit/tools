package com.bird.netty.core.message;

import com.bird.netty.core.constant.HandleType;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;


/**
 * @Author: bird
 * @Date: 2019/6/19 13:44
 */
@Data
public class ResolvedMessage<T> {

    private long timestamp;

    private ChannelHandlerContext ctx;

    private T obj;

    private String origin;

    private String application;

    private HandleType handleType;
}
