package com.bird.netty.core.message;

import lombok.Data;

/**
 * @Author: bird
 * @Date: 2019/6/18 18:20
 */
@Data
public class TypeMessage {

    public static final TypeMessage EMPTY = new TypeMessage();

    protected int type; //处理类型

    protected String data; //业务参数

    public TypeMessage() {
    }

    public TypeMessage(int type, String data) {
        this.type = type;
        this.data = data;
    }
}
