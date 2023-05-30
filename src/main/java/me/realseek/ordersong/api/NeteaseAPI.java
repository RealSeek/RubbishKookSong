package me.realseek.ordersong.api;

import me.realseek.ordersong.Main;
import me.realseek.ordersong.pojo.Netease;
import me.realseek.ordersong.timer.CheckNeteaseQRStatus;
import me.realseek.ordersong.util.apimethod.NeteaseMethod;

public class NeteaseAPI {
    /**
     * 网易云API登录方法
     */
    public static String neteaseLogin() {
        Main.getNetease().setUnikey(NeteaseMethod.getUnikey());
        Main.getNetease().setQRCodeUrl(NeteaseMethod.getQRCode(Main.getNetease().getUnikey()));
        return Main.getNetease().getQRCodeUrl();
    }

    /**
     * 网易云API获取歌曲ID方法
     * @param musicName
     * @return
     */
    public static int neteaseMusicId(String musicName){
        return NeteaseMethod.getMusicId(musicName);
    }

    /**
     * 网易云API获取歌曲详细信息
     * @param musicId
     */
    public static void neteaseMusicInfo(int musicId){
        NeteaseMethod.getMusicInfo(musicId);
    }

    /**
     * 网易云API获取歌曲下载URL
     * @param musicId
     * @return
     */
    public static String neteaseMusicDownloadUrl(int musicId){
        return NeteaseMethod.getMusicDownload(musicId);
    }
}
