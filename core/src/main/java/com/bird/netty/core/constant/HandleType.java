package com.bird.netty.core.constant;


import com.bird.netty.core.domain.LogMessage;

/**
 * @Author: bird
 * @Date: 2019/6/18 17:59
 */
public enum HandleType {
    DISCARD(-1, Object.class, 0),
    HEARTBEAT(1, String.class, 0),
    AS_MACHINE_SENDER(2, String.class, 1),
    COLLECT_OS_INFO(3, String.class, 2),
    COLLECT_APP_INFO(4, String.class, 2),
    LOG_ANALYZE(5, LogMessage.class, 2),
    ;

    HandleType(int mt, Class resolveType, int useFor) {
        this.mt = mt;
        this.resolveType = resolveType;
        this.useFor = useFor;
    }

    private int mt;
    private Class resolveType;

    private int useFor;//0通用,1客户端，2服务端

    public static HandleType of(int type) {
        for (HandleType v : values()) {
            if (v.mt == type) {
                return v;
            }
        }
        return null;
    }

    public int getMt() {
        return mt;
    }

    public Class getResolveType() {
        return resolveType;
    }

    public boolean isUseForServer() {
        return useFor == 0 || useFor == 2;
    }
}
