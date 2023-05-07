package me.realseek.ffmpeg;

import me.realseek.Main;
import me.realseek.timer.ProcessStatus;
import me.realseek.util.Card;
import me.realseek.util.DownloadMp3;
import me.realseek.util.JNAMethod;
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

    static String music;

    static TextChannelMessage botMessage;

    // 进入播放
    public void playMusic() throws IOException, InterruptedException {
        del = false;
        if (fist){
            // 原发送卡片
            fist = false;
        }else {
            botMessage.delete();
        }

        // 先下载
        music = DownloadMp3.downloadMp3();
        // 构建推流命令
        String playMusic = String.format
                ("%s -re -nostats -i \"%s\" -acodec libopus -ab 128k -f mpegts zmq:tcp://127.0.0.1:" + SimpleWebSocketListener.getFFmpegPort()
                        , Main.getFFmpegPath(), Main.getMp3Path());
        ProcessBuilder pb = new ProcessBuilder(playMusic.split(" "));
        pb.redirectErrorStream(true);
        // 启动
        playMusicProcess = pb.start();
        // 将播放音乐的 PID 储存到 config 内，用于出现意外在插件第二次启动时关闭上一次未关闭的进程
        // Main.getInstance().getConfig().set("MusicPID", JNAMethod.getProcessID(playMusicProcess));
        // 发送卡片
        msgMusicUUID = Main.getMessage().sendToSource(Card.playCard());
        botMessage = Main.getInstance().getCore().getUnsafe().getTextChannelMessage(msgMusicUUID);
        // 设置播放状态为开启
        Main.setPlayStatus(true);
        // 等待播放线程完毕
        playMusicProcess.waitFor();
        // 如果点击了下一首按钮
        if (!del) {
            if (Main.getMusicTitleList().size() > 0) {
                // 全部移除一个
                // 标题
                Main.getMusicTitleList().remove(0);
                // 封面
                Main.getMusicPicList().remove(0);
                // 歌曲链接
                if (music.equals("网易")) {
                    // 歌曲ID
                    Main.getMusicIdList().remove(0);
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

    public static String getMusic() {
        return music;
    }
}
