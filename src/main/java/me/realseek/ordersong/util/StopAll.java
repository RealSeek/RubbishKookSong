package me.realseek.ordersong.util;

import me.realseek.ordersong.Main;
import me.realseek.ordersong.ffmpeg.FFmpeg;
import me.realseek.ordersong.ffmpeg.PlayMusic;
import me.realseek.ordersong.voice.JoinVoice;

public class StopAll {
    public static void over(){
        // 关闭检测
        Main.getProcessStatus().close();
        // 关闭进程
        if (FFmpeg.getZMQ().isAlive()) {
            FFmpeg.getZMQ().destroy();
            if (PlayMusic.getPlayMusicProcess().isAlive()) {
                PlayMusic.getPlayMusicProcess().destroy();
            }
        }
        // 清空所有
        Main.getMusicList().clear();
        // 删除消息
        if (PlayMusic.getBotMessage() != null) {
            PlayMusic.getBotMessage().delete();
        }
        // 断开语音
        JoinVoice.disconnect();
    }
}
