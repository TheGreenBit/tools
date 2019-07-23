package com.bird.netty.core.domain;

import lombok.Data;

/**
 * @Author: bird
 * @Date: 2019/6/18 10:13
 */
@Data
public class OSInformation {
    private String id;

    private String ip;

    private String date;

    private long cpu;

    //cpu
    private String usrRate;
    private String sysRate;
    private String idleRate;


    //内存
    private long memTotal;
    private long memUse;
    private long memFree;

    private long threadCount;
    private long userCount;
    private long sysCount;


    private long swapTotal;
    private long swapUsed;
    private long swapFree;

    private long diskTotal;
    private long diskUsed;
    private long diskFree;


    private String wildcard;

}
