package me.realseek.timer;

import me.realseek.Main;
import me.realseek.ffmpeg.PlayMusic;
import me.realseek.util.Card;
import me.realseek.util.StopAll;

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
            Main.setPlayStatus(false);
            // 更新卡片
            // 删除消息
            PlayMusic.getBotMessage().delete();
            // 发送队列无播放歌曲的卡片
            PlayMusic.setMsgMusicUUID(Main.getMessage().sendToSource(Card.noPlayCard()));
            // 获取这条消息的uuid
            PlayMusic.setBotMessage(Main.getInstance().getCore().getUnsafe().getTextChannelMessage(PlayMusic.getMsgMusicUUID()));
        }
        timer.schedule(new DetectionTask(), 0, 1000);// 每秒执行一次任务
        timer.schedule(new CloseTask(), 30000); // 30秒后执行关闭
    }

    public void close() {
        timer.purge();
        timer.cancel();
    }

    class DetectionTask extends TimerTask {
        public void run() {
            if (!Main.getMusicTitleList().isEmpty()) {
                try {
                    // 启动推流
                    // System.out.println("检测列表内还有歌曲取消空闲计时");
                    // Main.setPlayStatus(true);
                    Main.getPlayMusic().playMusic();
                } catch (IOException | InterruptedException e) {
                    Main.getMessage().reply("出现致命错误，已清除所有队列");
                    // 关闭所有
                    StopAll.over();
                    throw new RuntimeException(e);
                }
                timer.cancel();
            }
        }
    }

    class CloseTask extends TimerTask {
        public void run() {
            // 关闭所有
            StopAll.over();
            System.out.println("感谢你的使用，为了保证不占用资源，已自动退出频道");
            timer.cancel();
        }
    }
}