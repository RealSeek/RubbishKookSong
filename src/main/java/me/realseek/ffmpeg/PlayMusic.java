package me.realseek.ffmpeg;

import me.realseek.Main;
import me.realseek.timer.ProcessStatus;
import me.realseek.util.Card;
import me.realseek.util.DownloadMp3;
import me.realseek.voice.SimpleWebSocketListener;
import snw.jkook.message.TextChannelMessage;

import java.io.IOException;

public class PlayMusic{
    // 播放音乐进程
    static Process playMusicProcess;

    // 判断是否为第一次进入
    static boolean fist;

    // 判断是否点击过下一首
    static boolean del;

    // 消息UUID
    static String msgMusicUUID;

    static TextChannelMessage botMessage;

    // 进入播放
    public void playMusic() throws IOException, InterruptedException {
        del = false;

        // 先下载
        if(DownloadMp3.downloadMp3()) {
            if (fist) {
                fist = false;
            }else {
                botMessage.delete();
            }
            // 发送卡片
            msgMusicUUID = Main.getMessage().sendToSource(Card.playCard());
            botMessage = Main.getInstance().getCore().getUnsafe().getTextChannelMessage(msgMusicUUID);

            // 构建推流命令
            String playMusic = String.format
                    ("%s -re -nostats -i \"%s\" -acodec libopus -ab 128k -f mpegts zmq:tcp://127.0.0.1:" + SimpleWebSocketListener.getFFmpegPort()
                            , Main.getFFmpegPath(), Main.getMp3Path());
            ProcessBuilder pb = new ProcessBuilder(playMusic.split(" "));
            pb.redirectErrorStream(true);
            // 启动
            playMusicProcess = pb.start();
            // 设置播放状态为开启
            Main.setPlayStatus(true);
            // 等待播放线程完毕
            playMusicProcess.waitFor();
            // 如果点击了下一首按钮
            if (!del) {
                if (Main.getMusicList().size() > 0) {
                    // 移除一个
                    Main.getMusicList().remove(0);
                }
            }
        }
        // 结束播放后开始⏲
        Main.setProcessStatus(new ProcessStatus());
        Main.getProcessStatus().start();
    }


    public static Process getPlayMusicProcess() {
        return playMusicProcess;
    }

    public static void setDel(boolean del) {
        PlayMusic.del = del;
    }

    public static TextChannelMessage getBotMessage() {
        return botMessage;
    }

    public static void setFist(Boolean fist) {
        PlayMusic.fist = fist;
    }

    public static void setMsgMusicUUID(String msgMusicUUID) {
        PlayMusic.msgMusicUUID = msgMusicUUID;
    }

    public static String getMsgMusicUUID() {
        return msgMusicUUID;
    }

    public static void setBotMessage(TextChannelMessage botMessage) {
        PlayMusic.botMessage = botMessage;
    }

    public static boolean isFist() {
        return fist;
    }
}
