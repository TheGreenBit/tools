package com.bird.netty.core.system;

import com.bird.netty.core.domain.AppInformation;
import sun.misc.VM;

import java.lang.management.*;
import java.util.List;

/**
 * @Author: bird
 * @Date: 2019/6/19 16:17
 */
public class AppInformationCollector {

    public static AppInformation getAppInfo() {
        AppInformation appInformation = new AppInformation();
        loadThread(appInformation);
        loadMemoryUsage(appInformation);
        loadGc(appInformation);
        appInformation.setLoadClazzCount(ManagementFactory.getClassLoadingMXBean().getLoadedClassCount());
        appInformation.setFinalRefCount(VM.getFinalRefCount());
        return appInformation;
    }


    private static void loadThread(AppInformation appInformation) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        appInformation.setThreadCount(threadMXBean.getThreadCount());
        long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
        appInformation.setDeadLockThreadCount(deadlockedThreads == null ? 0 : deadlockedThreads.length);
        appInformation.setDemonCount(threadMXBean.getDaemonThreadCount());
        appInformation.setPeakCount(threadMXBean.getPeakThreadCount());
    }

    private static void loadMemoryUsage(AppInformation appInformation) {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        appInformation.setHeapMax(heapMemoryUsage.getMax());
        appInformation.setHeapInit(heapMemoryUsage.getInit());
        appInformation.setHeapUse(heapMemoryUsage.getUsed());

        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        appInformation.setNoHeapMax(nonHeapMemoryUsage.getMax());
        appInformation.setNoHeapInit(nonHeapMemoryUsage.getInit());
        appInformation.setNoHeapUse(nonHeapMemoryUsage.getUsed());


    }

    private static void loadGc(AppInformation app) {
        long gcC = 0;
        long gcT = 0;
        List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();

        for (GarbageCollectorMXBean gc : garbageCollectorMXBeans) {
            gcC += gc.getCollectionCount();
            gcT += gc.getCollectionTime();
        }

        app.setGcCount(gcC);
        app.setGcTime(gcT);
    }


}
