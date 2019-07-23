package com.bird.netty.core.constant;

/**
 * @Author: bird
 * @Date: 2019/6/21 10:21
 */
@Deprecated
public enum LogLevel {
    TRANCE(1), DEBUG(2), INFO(3), WARN(4), ERROR(5);

    private int level;

    LogLevel(int level) {
        this.level = level;
    }


    public int getLevel() {
        return level;
    }}
