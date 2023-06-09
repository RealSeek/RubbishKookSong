package me.realseek.ordersong.listener;

import me.realseek.ordersong.Main;
import me.realseek.ordersong.ffmpeg.PlayMusic;
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
            Main.getMusicList().remove(0);
        }

        if (btn.equals("stop")){
            // 清空所有
            Main.getMusicList().clear();
            // 关闭推流的进程
            PlayMusic.getPlayMusicProcess().destroy();
        }
    }
}
