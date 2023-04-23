package me.realseek.api;

import me.realseek.util.apimethod.BilibiliMethod;

public class BilibiliAPI {

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
    public static String biliVideo(String BVId) {
        return BilibiliMethod.getBiliVideoUrl(BVId);
    }
}
