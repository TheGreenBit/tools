package com.bird.netty.client.handler;

import com.alibaba.fastjson.JSON;
import com.bird.netty.client.netty.DefaultMessageSender;
import com.bird.netty.core.constant.HandleType;
import com.bird.netty.core.domain.AppInformation;
import com.bird.netty.core.domain.OSInformation;
import com.bird.netty.core.handle.Handler;
import com.bird.netty.core.message.HandlerContext;
import com.bird.netty.core.message.TypeMessage;
import com.bird.netty.core.system.AppInformationCollector;
import com.bird.netty.core.system.OsInformationCollector;

import java.util.concurrent.TimeUnit;


/**
 * @Author: bird
 * @Date: 2019/6/19 10:42
 */
public class MachineStateSendHandler implements Handler<String> {
    @Override
    public TypeMessage handle(String s, HandlerContext ctx) {
        ctx.getCtx().channel().eventLoop()
                .scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String ip = OsInformationCollector.extractIP();
                            OSInformation osInformation = OsInformationCollector.get();
                            if (osInformation != null) {
                                osInformation.setIp(ip);
                                DefaultMessageSender.getInstance().sendIdle(HandleType.COLLECT_OS_INFO, JSON.toJSONString(osInformation));
                            }

                            AppInformation appInfo = AppInformationCollector.getAppInfo();
                            if (appInfo != null) {
                                appInfo.setIp(ip);
                                DefaultMessageSender.getInstance().sendIdle(HandleType.COLLECT_APP_INFO, JSON.toJSONString(appInfo));
                            }

                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 10, TimeUnit.SECONDS);
        return TypeMessage.EMPTY;
    }

    @Override
    public HandleType handleType() {
        return HandleType.AS_MACHINE_SENDER;
    }
}
