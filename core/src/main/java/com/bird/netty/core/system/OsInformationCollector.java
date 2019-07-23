package com.bird.netty.core.system;

import com.bird.netty.core.domain.OSInformation;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.*;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @Author: bird
 * @Date: 2019/6/19 14:53
 */
@Slf4j
public class OsInformationCollector {

    public static OSInformation get() {
        Sigar sigar = new Sigar();
        try {
            OSInformation osInformation = new OSInformation();
            loadMemory(osInformation, sigar.getMem());
            loadCpu(osInformation, sigar.getCpuPerc());
            loadThread(osInformation, sigar.getThreadCpu());
            loadSwap(osInformation, sigar.getSwap());
            loadDisk(osInformation, sigar);

            //loadNetSpeed(osInformation, sigar);
            return osInformation;
        } catch (Exception e) {
            log.error("获取机器状态信息出错：{}", e);
        } finally {
            sigar.close();
        }
        return null;
    }

    private static void loadMemory(OSInformation osInformation, Mem mem) throws Exception {
        osInformation.setMemFree(mem.getFree());
        osInformation.setMemTotal(mem.getTotal());
        osInformation.setMemUse(mem.getUsed());
    }

    private static void loadCpu(OSInformation osInformation, CpuPerc cpuPerc) {
        osInformation.setUsrRate(CpuPerc.format(cpuPerc.getUser()));
        osInformation.setSysRate(CpuPerc.format(cpuPerc.getSys()));
        osInformation.setIdleRate(CpuPerc.format(cpuPerc.getIdle()));
    }

    private static void loadThread(OSInformation osInformation, ThreadCpu tc) {
        osInformation.setThreadCount(tc.getTotal());
        osInformation.setSysCount(tc.getSys());
        osInformation.setUserCount(tc.getUser());
    }

    private static void loadSwap(OSInformation osInformation, Swap swap) {
        osInformation.setSwapTotal(swap.getTotal());
        osInformation.setSwapFree(swap.getFree());
        osInformation.setSwapUsed(swap.getUsed());
    }

    private static void loadDisk(OSInformation osInformation, Sigar sigar) throws Exception {
        FileSystem[] fileSystemList = sigar.getFileSystemList();
        long total = 0;
        long use = 0;
        long free = 0;
        for (FileSystem fileSystem : fileSystemList) {
            FileSystemUsage mountedFileSystemUsage = sigar.getMountedFileSystemUsage(fileSystem.getDevName());
            if (mountedFileSystemUsage != null) {
                total += mountedFileSystemUsage.getTotal();
                use += mountedFileSystemUsage.getUsed();
                free += mountedFileSystemUsage.getFree();
            }
        }

        osInformation.setDiskFree(free);
        osInformation.setDiskUsed(use);
        osInformation.setDiskTotal(total);
    }

    public static String extractIP() {
        String ip = null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return ip;
    }

    private static void loadNetSpeed(OSInformation osInformation, Sigar sigar) throws Exception {
        String[] netInterfaceList = sigar.getNetInterfaceList();
        String s = extractIP();
        double rxBytes = 0;
        double txBytes = 0;
        String description = null;
        // 一些其它的信息
        for (int i = 0; i < netInterfaceList.length; i++) {
            String netInterface = netInterfaceList[i];// 网络接口
            NetInterfaceConfig netInterfaceConfig = sigar.getNetInterfaceConfig(netInterface);

            if (netInterfaceConfig.getAddress().equals(s)) {

                description = netInterfaceConfig.getDescription();

                //System.out.println("网卡描述信息 =======" + description);
                double start = System.currentTimeMillis();
                NetInterfaceStat statStart = sigar.getNetInterfaceStat(netInterface);
                double rxBytesStart = statStart.getRxBytes();
                double txBytesStart = statStart.getTxBytes();

                Thread.sleep(1000);
                double end = System.currentTimeMillis();
                NetInterfaceStat statEnd = sigar.getNetInterfaceStat(netInterface);
                double rxBytesEnd = statEnd.getRxBytes();
                double txBytesEnd = statEnd.getTxBytes();

                rxBytes = ((rxBytesEnd - rxBytesStart) * 8 / (end - start) * 1000) / 1024;
                txBytes = ((txBytesEnd - txBytesStart) * 8 / (end - start) * 1000) / 1024;

                break;
            }

            // 判断网卡信息中是否包含VMware即虚拟机，不存在则设置为返回值
            //System.out.println("网卡MAC地址 ======="+netInterfaceConfig.getHwaddr());

        }

        //osInformation.setUpSpeed(String.format("%.0f", rxBytes));
        //osInformation.setDownSpeed(String.format("%.0f", txBytes));
        osInformation.setWildcard(description);
      /*  // 接收字节
        String rxBytess;
        // 发送字节
        String txBytess;

        if (rxBytes > 1024) {
            rxBytess = String.format("%.1f", rxBytes / 1024) + " Mbps";
        } else {
            rxBytess = String.format("%.0f", rxBytes) + " Kbps";
        }
        if (txBytes > 1024) {
            txBytess = String.format("%.1f", txBytes / 1024) + " Mbps";
        } else {
            txBytess = String.format("%.0f", txBytes) + " Kbps";
        }

        System.out.println("发送=======" + rxBytess);
        System.out.println("接收=======" + txBytess);*/
    }


}
