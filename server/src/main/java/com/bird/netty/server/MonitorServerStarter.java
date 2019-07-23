package com.bird.netty.server;

import com.bird.netty.server.boot.MonitorServerConfig;
import com.bird.netty.server.boot.MonitorServerHandler;
import com.bird.netty.server.boot.NettyStarter;
import com.bird.netty.core.handle.Handler;
import com.bird.netty.core.handle.HandlerRegistry;
import com.bird.netty.core.handle.HeartbeatHandler;
import io.netty.channel.ChannelHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

/**
 * @Author: bird
 * @Date: 2019/6/17 15:00
 */
@EnableTransactionManagement
@MapperScan(basePackages = {"com.bird.netty.server.dao"})
@SpringBootApplication(scanBasePackages = "com.bird.netty")
public class MonitorServerStarter {
    public static void main(String[] args) {
        /*String s = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        System.out.println("项目进程id为：" + s);*/
        new SpringApplicationBuilder()
                .web(false)
                .banner(new ResourceBanner(new ClassPathResource("banner")))
                .sources(MonitorServerStarter.class)
                .build()
                .run();
    }

    //@Bean
    public BeanDefinitionRegistryPostProcessor beanDefinitionRegistryPostProcessor(final ApplicationContext applicationContext) {
        return new BeanDefinitionRegistryPostProcessor() {

            @Override
            public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
                String[] beanDefinitionNames = registry.getBeanDefinitionNames();
                for (String s : beanDefinitionNames) {
                    System.out.println(s);
                }

             /*   Object appInfoCollectorDao = applicationContext.getBean("appInfoCollectorDao");
                System.out.println(appInfoCollectorDao);
*/
                /*Map<String, AppInfoCollectorDao> beansOfType = applicationContext.getBeansOfType(AppInfoCollectorDao.class);
                System.out.println(beansOfType);*/
            }

            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                //System.out.println("===========================");
            }
        };
    }


    @Bean
    public NettyStarter nettyStarter(MonitorServerConfig msc, ChannelHandler channelHandler) {
        return new NettyStarter(msc, channelHandler);
    }

    @Bean
    public HandlerRegistry handlerRegistry(List<Handler> handlerList) {
        HandlerRegistry handlerRegistry = new HandlerRegistry();
        handlerRegistry.registryAll(handlerList);
        handlerRegistry.registry(new HeartbeatHandler());
        return handlerRegistry;
    }

    @Bean
    public MonitorServerHandler monitorServerHandler(HandlerRegistry registry) {
        MonitorServerHandler handlerRegistry = new MonitorServerHandler(registry);
        return handlerRegistry;
    }


}
