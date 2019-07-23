package com.bird.netty.core.utils;

import java.util.UUID;

/**
 * @Author: bird
 * @Date: 2019/6/19 17:03
 */
public class IDGenerater {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
