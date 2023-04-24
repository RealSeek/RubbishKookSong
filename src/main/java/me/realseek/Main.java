package me.realseek;

import me.realseek.command.BotStatus;
import me.realseek.command.Help;
import me.realseek.command.bilibili.BilibiliAudio;
import me.realseek.command.netease.NeteaseAudio;
import me.realseek.command.netease.NeteaseAudioList;
import me.realseek.command.netease.NeteaseLogin;
import me.realseek.command.LeaveChannelCommand;
import me.realseek.command.qqmuisc.QQMusicAudio;
import me.realseek.command.qqmuisc.QQMusicCookie;
import me.realseek.ffmpeg.PlayMusic;
import me.realseek.listener.ButtonListener;
import me.realseek.pojo.Bilibili;
import me.realseek.pojo.Netease;
import me.realseek.pojo.QQMusic;
import me.realseek.timer.ProcessStatus;
import snw.jkook.JKook;
import snw.jkook.command.JKookCommand;
import snw.jkook.event.Listener;
import snw.jkook.message.Message;
import snw.jkook.message.TextChannelMessage;
import snw.jkook.plugin.BasePlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends BasePlugin {
    private static Main instance;
    private static String configPath;
    private static String ffmpegPath;
    private static String resPath;
    private static String token;
    static PlayMusic playMusic = new PlayMusic();
    // 网易云对象
    static Netease netease = new Netease();
    // QQ音乐对象
    static QQMusic qqMusic = new QQMusic();
    // Bilibili对象
    static Bilibili bilibili = new Bilibili();
    // 用于构建消息
    static Message message;
    // 判断播放状态
    static Boolean playStatus;
    // 存储歌曲ID
    static LinkedList<Integer> musicIdList = new LinkedList<Integer>();

    // 存储音乐标题
    static LinkedList<String> musicTitleList = new LinkedList<String>();

    // 存储音乐封面
    static LinkedList<String> musicPicList = new LinkedList<String>();
    // Bot第一次点歌的已添加消息UUID
    static String msgMuiscNoPlay;
    // Bot处于未点歌的消息
    static TextChannelMessage botMessageNoPlay;
    // Bot处于播放状态下的消息
    static TextChannelMessage botMessageInPlay;
    // Bot正在放歌的已添加消息uuid
    static String msgMuiscInPlay;
    // 用于存储消息列表
    static List<String> msgList = new ArrayList<>();
    // 进程计时器
    static ProcessStatus processStatus;

    @Override
    public void onLoad() {
        getLogger().info("插件初始化中...");
        instance = this;
        // 配置路径
        File res = new File(getDataFolder().getPath());
        File configFile = new File(getDataFolder().getPath() + "\\config.yml");
        File ffmpegFile = new File(getDataFolder().getPath() + "\\ffmpeg.exe");
        resPath = res.getAbsolutePath();
        configPath = configFile.getAbsolutePath();
        ffmpegPath = ffmpegFile.getAbsolutePath();

        // 获取token
        File kbc = new File("kbc.yml");
        // 创建一个 Scanner 对象来读取文件内容
        try (Scanner scanner = new Scanner(kbc)) {
            // 使用正则表达式匹配 token 的值
            Pattern pattern = Pattern.compile("token:\\s*\"(.*)\"");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    // 输出 token 的值
                    token = matcher.group(1);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 保存资源到 config
        if (!ffmpegFile.isFile()){
            System.out.println("未检测到ffmpeg，已为你重新加载");
            saveResource("ffmpeg.exe", false, false);
            saveDefaultConfig();
        }

        if (!configFile.isFile()){
            System.out.println("未检测到配置文件，已为你重新加载");
            saveDefaultConfig();
        }
        getLogger().info("初始化结束！");
    }

    @Override
    public void onEnable() {
        // 防止出现推流文件出现问题
        File mp3 = new File(Main.getResPath() + "\\radio.mp3");
        if (mp3.exists()){
            mp3.delete();
            System.out.println("检测到radio.mp3 , 为了确保第一次推流不会出现问题，已删除");
        }
        // 烂活
        getLogger().info("\n" +
                "______      _     _     _     _     _   __            _    _____                   \n" +
                "| ___ \\    | |   | |   (_)   | |   | | / /           | |  /  ___|                  \n" +
                "| |_/ /   _| |__ | |__  _ ___| |__ | |/ /  ___   ___ | | _\\ `--.  ___  _ __   __ _ \n" +
                "|    / | | | '_ \\| '_ \\| / __| '_ \\|    \\ / _ \\ / _ \\| |/ /`--. \\/ _ \\| '_ \\ / _` |\n" +
                "| |\\ \\ |_| | |_) | |_) | \\__ \\ | | | |\\  \\ (_) | (_) |   </\\__/ / (_) | | | | (_| |\n" +
                "\\_| \\_\\__,_|_.__/|_.__/|_|___/_| |_\\_| \\_/\\___/ \\___/|_|\\_\\____/ \\___/|_| |_|\\__, |\n" +
                "                                                                              __/ |\n" +
                "                                                                             |___/ \n");
        // 注册指令
        registerCommands();
        // 监听器
        addListener(new ButtonListener());

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    //方便监听器注册的封装
    private void addListener(Listener listener){
        JKook.getEventManager().registerHandlers(this,listener);
    }

    // 指令部分
    private void registerCommands() {
        new JKookCommand("帮助")
                .addAlias("help")
                .setDescription("帮助指南")
                .executesUser(new Help())
                .register(this);

        new JKookCommand("状态")
                .setDescription("查询Bot使用状态")
                .executesUser(new BotStatus())
                .register(this);
        // 调试功能
        new JKookCommand("停止")
                .setDescription("让机器人停止活动")
                .executesUser(new LeaveChannelCommand())
                .register(this);

        new JKookCommand("网易登录")
                .setDescription("网易云API登录")
                .executesUser(new NeteaseLogin())
                .register(this);

        new JKookCommand("网易")
                .setDescription("网易云点歌")
                .executesUser(new NeteaseAudio())
                .register(this);

        new JKookCommand("网易歌单")
                .setDescription("导入网易云歌单")
                .executesUser(new NeteaseAudioList())
                .register(this);

        new JKookCommand("qq刷新")
                .addAlias("QQ刷新")
                .setDescription("QQ音乐刷新cookie")
                .executesUser(new QQMusicCookie())
                .register(this);

        new JKookCommand("qq")
                .addAlias("QQ")
                .setDescription("QQ音乐点歌")
                .executesUser(new QQMusicAudio())
                .register(this);

        new JKookCommand( "b站")
                .setDescription("点播B站视频")
                .addAlias("B站")
                .addAlias("bili")
                .addAlias("Bili")
                .addAlias("bilibili")
                .addAlias("Bilibili")
                .addAlias("BILIBILI")
                .executesUser(new BilibiliAudio()
                )
                .register(this);

    }

    // Getter and Setter

    /**
     * 拿 Main
     * @return
     */
    public static Main getInstance() {
        return instance;
    }

    /**
     * 获取 config 文件夹路径
     * @return
     */
    public static String getResPath() {
        return resPath;
    }

    /**
     * 获取 FFmpeg 的路径
     * @return
     */

    public static String getFFmpegPath() {
        return ffmpegPath;
    }

    /**
     * 获取 KookBC 的机器人 token
     * @return
     */
    public static String getToken() {
        return token;
    }

    /**
     * 获取 netease 对象
     * @return
     */
    public static Netease getNetease() {
        return netease;
    }

    /**
     * 获取音乐标题的链接List
     * @return
     */
    public static LinkedList<String> getMusicTitleList() {
        return musicTitleList;
    }

    /**
     * 获取音乐封面的List
     * @return
     */
    public static LinkedList<String> getMusicPicList() {
        return musicPicList;
    }

    public static Bilibili getBilibili() {
        return bilibili;
    }

    public static Message getMessage() {
        return message;
    }

    public static void setMessage(Message message) {
        Main.message = message;
    }

    public static TextChannelMessage getBotMessageNoPlay() {
        return botMessageNoPlay;
    }

    public static void setBotMessageNoPlay(TextChannelMessage botMessageNoPlay) {
        Main.botMessageNoPlay = botMessageNoPlay;
    }


    public static PlayMusic getPlayMusic() {
        return playMusic;
    }

    /**
     * 获取未播放时回复你的”已添加“的消息的UUID
     * @return
     */
    public static String getMsgMuiscNoPlay() {
        return msgMuiscNoPlay;
    }

    /**
     * 设置未播放时回复你的”已添加“的消息的UUID
     * @param msgMuiscNoPlay
     */
    public static void setMsgMuiscNoPlay(String msgMuiscNoPlay) {
        Main.msgMuiscNoPlay = msgMuiscNoPlay;
    }
    /**
     * 获取正在播放时回复你的”已添加“的消息的UUID
     * @return
     */
    public static String getMsgMuiscInPlay() {
        return msgMuiscInPlay;
    }
    /**
     * 设置正在播放时回复你的”已添加“的消息的UUID
     * @param msgMuiscInPlay
     */
    public static void setMsgMuiscInPlay(String msgMuiscInPlay) {
        Main.msgMuiscInPlay = msgMuiscInPlay;
    }

    /**
     * 获取消息列表
     * @return
     */
    public static List<String> getMsgList() {
        return msgList;
    }

    /**
     * 获取Bot正在播放的消息
     * @return
     */
    public static TextChannelMessage getBotMessageInPlay() {
        return botMessageInPlay;
    }

    /**
     * 设置Bot正在播放的消息
     * @param botMessageInPlay
     */
    public static void setBotMessageInPlay(TextChannelMessage botMessageInPlay) {
        Main.botMessageInPlay = botMessageInPlay;
    }

    /**
     * 获取计时器
     * @return
     */
    public static ProcessStatus getProcessStatus() {
        return processStatus;
    }

    /**
     * 设置计时器
     * @param processStatus
     */
    public static void setProcessStatus(ProcessStatus processStatus) {
        Main.processStatus = processStatus;
    }

    /**
     * 存储歌曲ID
     * @return
     */
    public static LinkedList<Integer> getMusicIdList() {
        return musicIdList;
    }

    public static QQMusic getQqMusic() {
        return qqMusic;
    }

    public static Boolean getPlayStatus() {
        return playStatus;
    }

    public static void setPlayStatus(Boolean playStatus) {
        Main.playStatus = playStatus;
    }
}