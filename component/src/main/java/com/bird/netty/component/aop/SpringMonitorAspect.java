package com.bird.netty.component.aop;

import com.bird.netty.client.netty.DefaultMessageSender;
import com.bird.netty.component.Monitor;
import com.bird.netty.core.constant.HandleType;
import com.bird.netty.core.message.TypeMessage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @Author: bird
 * @Date: 2019/6/18 13:44
 */
@Aspect
public class SpringMonitorAspect {

    @Pointcut("@annotation(com.bird.netty.component.Monitor)")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object doMonitor(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return transaction(joinPoint, signature.getMethod());
    }

    public Object transaction(ProceedingJoinPoint pjp, Method method) throws Throwable {
        Monitor annotation = method.getAnnotation(Monitor.class);
        if (!annotation.messageSource().isInterface()) {
            TypeMessage message = annotation.messageSource().newInstance().getMessage(pjp);
            DefaultMessageSender.send(HandleType.of(message.getType()), message.getData());
        } else {
            //TODO 
        }
        /*Transaction t = Cat.newTransaction("method", method.getName());
        t.setTimestamp(System.currentTimeMillis());
        try {
            Object proceed = pjp.proceed();
            t.setSuccessStatus();
            return proceed;
        } catch (Throwable e) {
            t.setStatus(e);
            //Cat.logError(e);
            throw e;
        } finally {
            t.setDurationInMillis(System.currentTimeMillis() - t.getTimestamp());
            t.complete();
        }*/
        //@TODO

        return null;
    }
}
