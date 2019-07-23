package com.bird.netty.core.message;

import com.bird.netty.core.constant.HandleType;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: bird
 * @Date: 2019/6/17 15:33
 */
@Data
public class IMessage extends TypeMessage {

    private String application;

    private long timestamp;

    public boolean hasData() {
        return !StringUtils.isEmpty(data);
    }

    public static IMessage of(TypeMessage typeMessage) {
        return of(HandleType.of(typeMessage.getType()), typeMessage.getData());
    }

    public static IMessage of(HandleType ht, String data) {
        if (ht == null) {
            throw new IllegalArgumentException("错误的参数处理类型！");
        }
        IMessage iMessage = new IMessage();
        iMessage.setTimestamp(System.currentTimeMillis());
        iMessage.setData(data);
        iMessage.setType(ht.getMt());
        return iMessage;
    }

    public static IMessage serverRespond(TypeMessage typeMessage) {
        IMessage iMessage = new IMessage();
        iMessage.setApplication("Server");
        iMessage.setTimestamp(System.currentTimeMillis());

        iMessage.setData(typeMessage.getData());
        iMessage.setType(typeMessage.getType());
        return iMessage;
    }

    public static IMessage heartbeat(String application) {
        IMessage iMessage = new IMessage();
        iMessage.setApplication(application);
        iMessage.setTimestamp(System.currentTimeMillis());

        iMessage.setData("heartbeat");
        iMessage.setType(HandleType.HEARTBEAT.getMt());
        return iMessage;
    }


    @Override
    public String toString() {
        return "IMessage{" +
                "application='" + application + '\'' +
                ", timestamp=" + timestamp +
                ", type=" + type +
                ", data='" + data + '\'' +
                '}';
    }
}
