package me.realseek.ordersong.util;

import cn.hutool.http.Header;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import me.realseek.ordersong.Main;
import me.realseek.ordersong.api.BilibiliAPI;
import me.realseek.ordersong.api.NeteaseAPI;
import me.realseek.ordersong.api.QQMusicAPI;
import me.realseek.ordersong.ffmpeg.PlayMusic;
import me.realseek.ordersong.pojo.Bilibili;
import me.realseek.ordersong.pojo.Netease;
import me.realseek.ordersong.pojo.QQMusic;

import java.io.File;

public class DownloadMp3 {
    static File file = new File(Main.getResPath() + "\\radio.mp3");
    static boolean fileState = true;
    /**
     * 用于下载歌曲文件
     *
     */
    public static boolean downloadMp3(){
        fileState = true;
        if (file.exists()){
            file.delete();
            // System.out.println("已删除文件");
        }

        Object musicOBJ;
        if (!Main.getMusicList().isEmpty()) {
            musicOBJ = Main.getMusicList().getFirst();
        }else {
            return false;
        }

        // QQ音乐有概率出现获取到 url 但是 403
        try {
            // 判断
            if (musicOBJ instanceof QQMusic) {
                QQMusic qqMusic = (QQMusic) musicOBJ;
                // QQ音乐下载
                String musicDownloadUrl = QQMusicAPI.qqMusicDownloadUrl(qqMusic.getSongmid());
                HttpUtil.downloadFile(musicDownloadUrl, file);
                return true;
            } else if (musicOBJ instanceof Bilibili) {
                Bilibili bilibili = (Bilibili) musicOBJ;
                // B站下载
                String musicDownloadUrl = BilibiliAPI.biliVideo(bilibili.getBvNum(), bilibili.getCid());
                HttpRequest.get(musicDownloadUrl)
                        .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                        .header(Header.REFERER, "https://www.bilibili.com")
                        .execute().writeBody(file);
                return true;
            } else {
                Netease netease = (Netease) musicOBJ;
                // 网易云下载
                String musicDownloadUrl = NeteaseAPI.neteaseMusicDownloadUrl(netease.getMuiscId());
                HttpUtil.downloadFile(musicDownloadUrl, file);
                return true;
            }
        } catch (HttpException e) {
            System.out.println("出现下载403，已跳过");
            // 设置手动下一首的状态为开
            PlayMusic.setDel(true);
            // 关闭推流的进程
            // PlayMusic.getPlayMusicProcess().destroy();
            // 移除List的第一位
            Main.getMusicList().remove(0);
            downloadMp3();
            return false;
            // throw new RuntimeException(e);
        }
    }
}
