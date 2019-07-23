package com.bird.netty.test;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @Author: bird
 * @Date: 2019/6/20 13:22
 */
public class ConfigTest {

    @Test
    public void test001() throws IOException {
        Properties properties = new Properties();
        ClassPathResource resource = new ClassPathResource("bs.properties");
        if (resource.exists()) {
            PropertiesLoaderUtils.fillProperties(properties,resource);
        }

        PropertiesLoaderUtils.fillProperties(properties,new ClassPathResource("application.properties"));
        System.out.println(properties);
    }
}
