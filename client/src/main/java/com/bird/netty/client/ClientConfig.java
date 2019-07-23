package com.bird.netty.client;

import lombok.Data;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @Author: bird
 * @Date: 2019/6/18 14:53
 */
@Data
public class ClientConfig {

    private static final String prefix = "com.bs.monitor.";

    private static final ClientConfig clientConfig = new ClientConfig();

    private String hostAndPort = "localhost:29999";

    private String application;

    //默认
    private int machineInterval = 10;

    private int queueBuffer = 2048;

    private static boolean init = false;

    private ClientConfig() {

    }

    public static String getConfigApplicationName() {
        return getInstance().getApplication();
    }

    public static String hostAndPort() {
        return getInstance().getHostAndPort();
    }

    public static int queenBuffer() {
        return getInstance().getQueueBuffer();
    }

    public static ClientConfig getInstance() {
        if (!init) {
            try {
                initConfig();
            } catch (Exception e) {
                throw new IllegalStateException("monitor监控配置有误！");
            }
        }

        return clientConfig;
    }

    private synchronized static void initConfig() throws Exception {
        if (!init) {
            Properties properties = new Properties();
            fillProperties("bosssoft.properties", properties);
            fillProperties("application.properties", properties);
            fillProperties("monitor.properties", properties);

            Set<Map.Entry<Object, Object>> entries = properties.entrySet();
            BeanMap beanMap = new BeanMap(clientConfig);
            for (Map.Entry<Object, Object> e : entries) {
                String key = (String) e.getKey();
                if (key != null && key.startsWith(prefix)) {
                    String replace = key.replace(prefix, "");
                    if (beanMap.containsKey(replace) && e.getValue() != null) {
                        beanMap.put(replace, e.getValue());
                    }
                }
            }

            System.out.println(clientConfig);

            if (StringUtils.isAnyEmpty(clientConfig.getHostAndPort(), clientConfig.getApplication())) {
                throw new IllegalStateException(String.format("配置monitor server hostAndPort：%s,application:%s不能为空！",clientConfig.getHostAndPort(), clientConfig.getApplication()));
            }

            init = true;
        }
    }


    private static void fillProperties(String conf, Properties prop) throws Exception {
        ClassPathResource resource = new ClassPathResource(conf);
        if (resource.exists() && resource.isReadable()) {
            PropertiesLoaderUtils.fillProperties(prop, resource);
        }
    }

}
