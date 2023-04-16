package me.realseek.timer;

import me.realseek.Main;
import me.realseek.ffmpeg.FFmpeg;
import me.realseek.ffmpeg.PlayMusic;
import me.realseek.voice.JoinVoice;
import me.realseek.voice.SimpleWebSocketListener;
import snw.jkook.message.TextChannelMessage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ProcessStatus {
    private Timer timer;

    public ProcessStatus() {
        timer = new Timer();
    }

    public void start() {
        if (Main.getMusicTitleList().isEmpty()) {
            System.out.println("检测到列表为空，为避免不必要的麻烦，请手动输入/停止或等待机器人自动退出");
        }
        timer.schedule(new DetectionTask(), 0, 1000);// 每秒执行一次任务
        timer.schedule(new CloseTask(), 30000); // 60秒后执行关闭
    }

    public void close() {
        timer.cancel();
    }

    class DetectionTask extends TimerTask {
        public void run() {
            if (!Main.getMusicTitleList().isEmpty()) {
                try {
                    // 启动推流
                    System.out.println("检测列表内还有歌曲取消空闲计时");
                    Main.getPlayMusic().playMusic();
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                timer.cancel();
            }
        }
    }

    class CloseTask extends TimerTask {
        public void run() {
            JoinVoice.disconnect();
            // 关闭进程
            if (FFmpeg.getZMQ().isAlive()) {
                FFmpeg.getZMQ().destroy();
                PlayMusic.getPlayMusicProcess().destroy();
            }
            // 删除消息
            if (PlayMusic.getBotMessage1() != null){
                PlayMusic.getBotMessage1().delete();
                PlayMusic.getBotMessage2().delete();
            }
            // 清空所有
            // 标题
            Main.getMusicTitleList().clear();
            // 封面
            Main.getMusicPicList().clear();
            // 歌曲链接
            // Main.getMusicUrlList().clear();
            System.out.println("感谢你的使用，为了保证不占用资源，已自动退出频道");
            timer.cancel();
        }
    }
}