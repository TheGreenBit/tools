package com.bird.netty.core.domain;

import lombok.Data;


/**
 * @Author: bird
 * @Date: 2019/6/19 14:40
 */
@Data
public class AppInformation {

    private String id;
    private String appName;
    private String date;
    private String ip;

    private int threadCount;
    private int demonCount;
    private int peakCount;

    private long heapMax;
    private long heapUse;
    private long heapInit;

    private long noHeapMax;
    private long noHeapInit;
    private long noHeapUse;

    private int deadLockThreadCount;


    private int loadClazzCount;

    private long gcCount;
    private long gcTime;

    private int finalRefCount;
}
