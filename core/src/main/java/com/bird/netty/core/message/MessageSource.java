package com.bird.netty.core.message;

/**
 * @Author: bird
 * @Date: 2019/6/21 18:05
 */
public interface MessageSource<S> {
    TypeMessage getMessage(S s);
}
