package me.realseek.listener;

import me.realseek.Main;
import me.realseek.ffmpeg.PlayMusic;
import snw.jkook.event.EventHandler;
import snw.jkook.event.Listener;
import snw.jkook.event.user.UserClickButtonEvent;

public class ButtonListener implements Listener {
    @EventHandler
    public void ButtonEvent(UserClickButtonEvent event) {
        String btn = event.getValue();
        // 当用户点击下一首歌
        if (btn.equals("nextMusic")){
            // 设置手动下一首的状态为开
            PlayMusic.setDel(true);
            // 关闭推流的进程
            PlayMusic.getPlayMusicProcess().destroy();
            // 移除List的第一位
            // 标题
            Main.getMusicTitleList().remove(0);
            // 封面
            Main.getMusicPicList().remove(0);
            // 歌曲链接
            // Main.getMusicUrlList().remove(0);
            // 歌曲ID
            if (PlayMusic.getMusic().equals("网易")) {
                Main.getMusicIdList().remove(0);
            }
        }

        if (btn.equals("stop")){
            // 清空所有
            // 标题
            Main.getMusicTitleList().clear();
            // 封面
            Main.getMusicPicList().clear();
            // 歌曲ID
            Main.getMusicIdList().clear();
            // 关闭推流的进程
            PlayMusic.getPlayMusicProcess().destroy();
        }
    }
}
