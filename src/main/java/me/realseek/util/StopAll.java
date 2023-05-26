package me.realseek.util;

import me.realseek.Main;
import me.realseek.ffmpeg.FFmpeg;
import me.realseek.ffmpeg.PlayMusic;
import me.realseek.voice.JoinVoice;

public class StopAll {
    public static void over(){
        // 清空所有
        Main.getMusicList().clear();
        // 关闭检测
        Main.getProcessStatus().close();
        // 关闭进程
        if (FFmpeg.getZMQ().isAlive()) {
            FFmpeg.getZMQ().destroy();
            PlayMusic.getPlayMusicProcess().destroy();
        }
        // 删除消息
        if (PlayMusic.getBotMessage() != null) {
            PlayMusic.getBotMessage().delete();
        }
        // 断开语音
        JoinVoice.disconnect();
    }
}
