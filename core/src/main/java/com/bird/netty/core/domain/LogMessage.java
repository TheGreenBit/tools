package com.bird.netty.core.domain;

import lombok.Data;

/**
 * @Author: bird
 * @Date: 2019/6/21 17:02
 */
@Data
public class LogMessage {
    private int level;
    private String className;
    private String threadName;
    private String message;
    private long timestamp;
    private String args;
}
