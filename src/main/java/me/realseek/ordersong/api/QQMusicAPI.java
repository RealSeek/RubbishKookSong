package me.realseek.ordersong.api;

import me.realseek.ordersong.util.apimethod.QQMusicMethod;

public class QQMusicAPI {
    /**
     * 刷新Cookie
     */
    public static void qqMusicCookie(){
        QQMusicMethod.setCookie();
    }
    /**
     * 搜索获取歌曲信息
     */
    public static void qqMusicSearch(String singName){
        QQMusicMethod.getQQMusicInfo(singName);
    }

    /**
     * 下载歌曲
     */
    public static String qqMusicDownloadUrl(String mid){
        return QQMusicMethod.getDownloadUrl(mid);
    }
}
