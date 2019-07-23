package com.bird.netty.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: bird
 * @Date: 2019/6/19 17:06
 */
public class DateUtil {

    public static String format(long time) {
        Date date = new Date(time);
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        return format;
    }

}
