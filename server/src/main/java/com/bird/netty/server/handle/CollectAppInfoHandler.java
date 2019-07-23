package com.bird.netty.server.handle;

import com.bird.netty.core.constant.HandleType;
import com.bird.netty.core.domain.AppInformation;
import com.bird.netty.core.handle.Handler;
import com.bird.netty.core.message.HandlerContext;
import com.bird.netty.core.message.TypeMessage;
import com.bird.netty.core.utils.DateUtil;
import com.bird.netty.core.utils.IDGenerater;
import com.bird.netty.server.dao.AppInfoCollectorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Author: bird
 * @Date: 2019/6/19 16:57
 */
@Component
public class CollectAppInfoHandler implements Handler<AppInformation> {

    @Autowired
    AppInfoCollectorDao appInfoCollectorDao;


    @Override
    public TypeMessage handle(AppInformation obj, HandlerContext ctx) {
        obj.setId(IDGenerater.uuid());
        obj.setAppName(ctx.getApplication());
        obj.setDate(DateUtil.format(ctx.getTimestamp()));
        System.out.println("received "+obj);
        //appInfoCollectorDao.save(obj);
        return TypeMessage.EMPTY;
    }

    @Override
    public HandleType handleType() {
        return HandleType.COLLECT_APP_INFO;
    }
}
