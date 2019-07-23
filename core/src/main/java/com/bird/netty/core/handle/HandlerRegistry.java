package com.bird.netty.core.handle;

import com.bird.netty.core.constant.HandleType;
import com.bird.netty.core.message.HandlerContext;
import com.bird.netty.core.message.TypeMessage;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @Author: bird
 * @Date: 2019/6/18 11:03
 */
public class HandlerRegistry {

    IntObjectMap<ResolvedHandler> handlers = new IntObjectHashMap<>();

    public HandlerRegistry() {
        registry(new HeartbeatHandler());
        registry(new DiscardHandler());
    }

    public synchronized void registry(Handler handler) {
        handlers.put(handler.handleType().getMt(), new ResolvedHandler(handler));
    }

    public void registryAll(Collection<Handler> rHandlers) {
        for (Handler handler : rHandlers) {
            handlers.put(handler.handleType().getMt(), new ResolvedHandler(handler));
        }
    }


    public ResolvedHandler getHandler(int mt) {
        return handlers.get(mt);
    }


    public static class ResolvedHandler<T> implements Handler {

        private final Handler<T> handler;

        private Class<T> type;

        private ResolvedHandler(Handler handler) {
            assert handler != null : "处理器不能为空！";
            this.handler = handler;
            this.type = resolveGenericType();
        }

        @Override
        public TypeMessage handle(Object obj, HandlerContext ext) {
            return this.handler.handle((T) obj, ext);
        }

        @Override
        public HandleType handleType() {
            return this.handler.handleType();
        }

        private Class resolveGenericType() {
            Class c = handler.getClass();
            while (true) {
                Method[] declaredMethods = c.getDeclaredMethods();
                for (Method m : declaredMethods) {
                    if ("handle".equalsIgnoreCase(m.getName()) && !m.isBridge() && m.getParameterTypes().length == 2) {
                        return m.getParameterTypes()[0];
                    }
                }

                c = c.getSuperclass();
                if (c.getName().startsWith("java") || c.getName().startsWith("com.sun") || c.getName().startsWith("javax")) {
                    break;
                }

            }

            throw new IllegalArgumentException("未发现处理器类型");
        }

        public Class<T> getType() {
            return type;
        }
    }


    public int size() {
        return handlers.size();
    }

}
