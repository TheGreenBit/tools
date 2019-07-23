package com.bird.netty.server.handle;

import com.bird.netty.core.constant.HandleType;
import com.bird.netty.core.domain.OSInformation;
import com.bird.netty.core.handle.Handler;
import com.bird.netty.core.message.HandlerContext;
import com.bird.netty.core.message.TypeMessage;
import com.bird.netty.core.utils.DateUtil;
import com.bird.netty.core.utils.IDGenerater;
import com.bird.netty.server.dao.MachineCollectorDao;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: bird
 * @Date: 2019/6/19 16:57
 */
@Component
public class CollectMachineHandler implements Handler<OSInformation> {

    @Resource(name = "machineCollectorDao")
    private MachineCollectorDao machineCollectorDao;

    @Override
    public TypeMessage handle(OSInformation obj, HandlerContext ctx) {
        obj.setId(IDGenerater.uuid());
        obj.setDate(DateUtil.format(ctx.getTimestamp()));
        System.out.println("received " + obj);
        // machineCollectorDao.save(obj); 编写sql
        return TypeMessage.EMPTY;
    }

    @Override
    public HandleType handleType() {
        return HandleType.COLLECT_OS_INFO;
    }
}
