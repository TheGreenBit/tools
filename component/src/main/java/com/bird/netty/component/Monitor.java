package com.bird.netty.component;

import com.bird.netty.core.message.MessageSource;

import java.lang.annotation.*;

/**
 * @Author: bird
 * @Date: 2019/6/18 13:41
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Monitor {

    Class<? extends MessageSource> messageSource() default MessageSource.class;
}
