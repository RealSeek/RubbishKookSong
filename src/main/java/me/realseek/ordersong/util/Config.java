package me.realseek.ordersong.util;

import me.realseek.ordersong.Main;
import snw.jkook.plugin.BasePlugin;

/**
 * @author xiaoACE
 */
public class Config {

    static BasePlugin plugin = Main.getInstance();

    /**
     * 从配置里获取网易云音乐 API 链接
     *
     * @author xiaoACE
     * @return {@code String} 网易云音乐API链接
     */
    public static String getNeteaseAPIURL(){
        return plugin.getConfig().getString("Netease_API_URL");
    }

    /**
     * 从配置里获取 QQ音乐 API 链接
     *
     * @author xiaoACE
     * @return {@code String} QQ音乐 API 链接
     */
    public static String getQQMusicAPIURL(){
        return plugin.getConfig().getString("QQMusic_API_URL");
    }

    /**
     * 从配置文件里获取 Bilibili Cookie
     * @author RealSeek
     * @return {@code String} Bilibili Cookie
     */
    public static String getBilibiliCookie() {
        return plugin.getConfig().getString("Bilibili_Cookie");
    }
}
