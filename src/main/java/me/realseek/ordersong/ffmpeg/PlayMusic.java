package me.realseek.ordersong.ffmpeg;

import me.realseek.ordersong.Main;
import me.realseek.ordersong.timer.ProcessStatus;
import me.realseek.ordersong.util.Card;
import me.realseek.ordersong.util.DownloadMp3;
import me.realseek.ordersong.util.SystemType;
import me.realseek.ordersong.voice.SimpleWebSocketListener;
import snw.jkook.message.TextChannelMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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
            botMessage = Main.getInstance().getCore().getHttpAPI().getTextChannelMessage(msgMusicUUID);

            // 根据系统判断路径
            String os = "Windows";
            String mp3Path;
            if (os.equals(SystemType.getOperatingSystemType())){
                mp3Path = "\"" + Main.getMp3Path() + "\"";
            }else mp3Path = "radio.mp3";

            // 构建推流命令
            String playMusic = String.format
                    ("%s -re -nostats -i %s -acodec libopus -ab 128k -f mpegts zmq:tcp://127.0.0.1:" + SimpleWebSocketListener.getFFmpegPort()
                            , Main.getFFmpegPath(), mp3Path);

            // System.out.println(playMusic);

            ProcessBuilder pb = new ProcessBuilder(playMusic.split(" "));
            pb.directory(new File(Main.getResPath()));
            // pb.redirectErrorStream(true);
            // 启动
            playMusicProcess = pb.start();
            // System.out.println("启动完成");
            // 设置播放状态为开启
            Main.setPlayStatus(true);
            // 等待播放线程完毕
            playMusicProcess.waitFor();
            // 正常播放下一首按钮
            if (!del) {
                if (Main.getMusicList().size() > 0) {
                    // 移除一个
                    Main.getMusicList().remove(0);
                    // System.out.println("被移除力QAQ");
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
