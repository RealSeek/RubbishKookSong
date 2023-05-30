package me.realseek.ordersong.api;

import me.realseek.ordersong.Main;
import me.realseek.ordersong.pojo.Bilibili;
import me.realseek.ordersong.timer.CheckBiliQRStatus;
import me.realseek.ordersong.util.apimethod.BilibiliMethod;

public class BilibiliAPI {
    /**
     * B站扫码登录（防止被反扒标记）
     */
    public static String biliLogin(){
        // 获取到扫码登录的 url 和 key
        return BilibiliMethod.getBiliLoginUrl();
    }

    /**
     * 获取B站视频详细信息
     *
     * @param BVId
     */
    public static void biliVideoInfo(String BVId){
        BilibiliMethod.getBiliVideoInfo(BVId);
    }

    /**
     * 获取B站视频音频流
     * @param BVId
     * @return
     */
    public static String biliVideo(String BVId,int cid) {
        return BilibiliMethod.getBiliVideoUrl(BVId,cid);
    }
}
