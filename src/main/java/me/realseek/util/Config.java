package me.realseek.util;

import me.realseek.Main;
import snw.jkook.plugin.BasePlugin;

/**
 * @author xiaoACE
 */
public class Config {

    static BasePlugin plugin = Main.getInstance();

    /**
     * 从配置里获取网易云音乐API链接
     *
     * @author xiaoACE
     * @return {@code String} 网易云音乐API链接
     */
    public static String getNeteaseAPIURL(){
        return plugin.getConfig().getString("Netease_API_URL");
    }

    /**
     * 从配置里获取QQ音乐API链接
     *
     * @author xiaoACE
     * @return {@code String} QQ音乐API链接
     */
    public static String getQQMusicAPIURL(){
        return plugin.getConfig().getString("QQMusic_API_URL");
    }
}
