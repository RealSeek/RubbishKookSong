package me.realseek.util;

public class TimeStamp {
    /**
     * 生成一个随机数 用于填入网易云API请求
     * @return
     */
    public static String spawnTimeStamp(){
        long timestamp = System.currentTimeMillis();
        return "&timestamp=" + String.valueOf(timestamp);
    }
}
