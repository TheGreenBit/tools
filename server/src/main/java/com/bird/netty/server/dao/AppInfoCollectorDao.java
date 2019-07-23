package com.bird.netty.server.dao;

import com.bird.netty.core.domain.AppInformation;

/**
 * @Author: bird
 * @Date: 2019/6/19 17:00
 */
public interface AppInfoCollectorDao {
    void save(AppInformation obj);
}
