package me.realseek.util;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import me.realseek.Main;
import me.realseek.api.BilibiliAPI;
import me.realseek.api.NeteaseAPI;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadMp3 {
    static File file = new File(Main.getResPath() + "\\radio.mp3");
    static boolean fileState = true;
    /**
     * 用于下载歌曲文件
     *
     */
    public static String downloadMp3() throws InterruptedException {
        fileState = true;
        if (file.exists()){
            file.delete();
            System.out.println("已删除文件");
        }
        // 判断url来源
        Pattern url = Pattern.compile("B站");
        Matcher m = url.matcher(Main.getMusicTitleList().get(0));
        if (!m.find()) {
            // 网易云下载
            String musicDownloadUrl = NeteaseAPI.neteaseMusicDownloadUrl(Main.getMusicIdList().get(0));
            HttpUtil.downloadFile(musicDownloadUrl, file);
            return "网易";
        }else {
            // B站下载
            String musicDownloadUrl = BilibiliAPI.biliVideo(Main.getBilibili().getBvNum());
            HttpRequest.get(musicDownloadUrl)
                    .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                    .header(Header.REFERER, "https://www.bilibili.com")
                    .execute().writeBody(file);
            return "B站";
        }
    }
}
