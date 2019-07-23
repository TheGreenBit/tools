package com.bird.netty.server.boot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: bird
 * @Date: 2019/6/17 15:12
 */
@ConfigurationProperties(prefix = "com.bs.monitor")
@Data
@Component
public class MonitorServerConfig {

    private int port;



}
