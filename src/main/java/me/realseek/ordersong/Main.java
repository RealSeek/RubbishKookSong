package me.realseek.ordersong;

import me.realseek.ordersong.command.BotStatus;
import me.realseek.ordersong.command.Help;
import me.realseek.ordersong.command.LeaveChannelCommand;
import me.realseek.ordersong.command.bilibili.BilibiliAudio;
import me.realseek.ordersong.command.bilibili.BilibiliLogin;
import me.realseek.ordersong.command.netease.NeteaseAudio;
import me.realseek.ordersong.command.netease.NeteaseAudioList;
import me.realseek.ordersong.command.netease.NeteaseLogin;
import me.realseek.ordersong.command.qqmuisc.QQMusicAudio;
import me.realseek.ordersong.command.qqmuisc.QQMusicCookie;
import me.realseek.ordersong.ffmpeg.PlayMusic;
import me.realseek.ordersong.listener.ButtonListener;
import me.realseek.ordersong.pojo.Bilibili;
import me.realseek.ordersong.pojo.Netease;
import me.realseek.ordersong.pojo.QQMusic;
import me.realseek.ordersong.timer.ProcessStatus;
import me.realseek.ordersong.util.FileInit;
import snw.jkook.JKook;
import snw.jkook.command.JKookCommand;
import snw.jkook.event.Listener;
import snw.jkook.message.Message;
import snw.jkook.plugin.BasePlugin;

import java.util.LinkedList;

public class Main extends BasePlugin {
    private static Main instance;
    private static String ffmpegPath;
    private static String resPath;
    private static String menuPath;
    private static String mp3Path;
    private static String token;
    static PlayMusic playMusic = new PlayMusic();

    // 网易云对象
    static Netease netease = new Netease();

    // QQ音乐对象
    static QQMusic qqMusic = new QQMusic();

    // Bilibili对象
    static Bilibili bilibili = new Bilibili();

    /**
     * 二维码的消息uuid
     */
    static String loginMsg;

    // 用于构建消息
    static Message message;

    // 判断播放状态
    static Boolean playStatus;

    // Bot正在放歌的已添加消息uuid
    static String botMessageUUID;

    // 进程计时器
    static ProcessStatus processStatus;

    // 歌曲对象集合
    static LinkedList<Object> musicList = new LinkedList<Object>();

    @Override
    public void onLoad() {
        getLogger().info("插件初始化中...");
        instance = this;
        // 初始化
        FileInit.FileInit();
        getLogger().info("初始化结束！");
    }

    @Override
    public void onEnable() {
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
        System.out.println("点歌插件已卸载，感谢你的使用");
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
                .addAlias("nlogin")
                .executesUser(new NeteaseLogin())
                .register(this);

        new JKookCommand("网易")
                .addAlias("wy")
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

        new JKookCommand("b站登录")
                .setDescription("Bilibili扫码登录")
                .addAlias("B站登录")
                .addAlias("bili登录")
                .addAlias("Bili登录")
                .addAlias("bilibili登录")
                .addAlias("Bilibili登录")
                .addAlias("BILIBILI登录")
                .addAlias("blogin")
                .executesUser(new BilibiliLogin())
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
     * 获取 radio.mp3 的路径
     */
    public static String getMp3Path(){
        return mp3Path;
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

    public static Bilibili getBilibili() {
        return bilibili;
    }

    public static Message getMessage() {
        return message;
    }

    public static void setMessage(Message message) {
        Main.message = message;
    }

    public static PlayMusic getPlayMusic() {
        return playMusic;
    }

    /**
     * 获取正在播放时回复你的”已添加“的消息的UUID
     * @return
     */
    public static String getBotMessageUUID() {
        return botMessageUUID;
    }
    /**
     * 设置正在播放时回复你的”已添加“的消息的UUID
     * @param botMessageUUID
     */
    public static void setBotMessageUUID(String botMessageUUID) {
        Main.botMessageUUID = botMessageUUID;
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

    public static QQMusic getQqMusic() {
        return qqMusic;
    }

    public static Boolean getPlayStatus() {
        return playStatus;
    }

    public static void setPlayStatus(Boolean playStatus) {
        Main.playStatus = playStatus;
    }

    public static String getMenuPath() {
        return menuPath;
    }

    public static LinkedList<Object> getMusicList() {
        return musicList;
    }

    public static String getLoginMsg() {
        return loginMsg;
    }

    public static void setLoginMsg(String loginMsg) {
        Main.loginMsg = loginMsg;
    }

    public static void setFfmpegPath(String ffmpegPath) {
        Main.ffmpegPath = ffmpegPath;
    }

    public static void setResPath(String resPath) {
        Main.resPath = resPath;
    }

    public static void setMenuPath(String menuPath) {
        Main.menuPath = menuPath;
    }

    public static void setMp3Path(String mp3Path) {
        Main.mp3Path = mp3Path;
    }

    public static void setToken(String token) {
        Main.token = token;
    }
}