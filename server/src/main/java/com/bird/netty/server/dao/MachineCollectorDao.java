package com.bird.netty.server.dao;

import com.bird.netty.core.domain.OSInformation;

/**
 * @Author: bird
 * @Date: 2019/6/19 17:00
 */
public interface MachineCollectorDao {
    void save(OSInformation obj);
}
