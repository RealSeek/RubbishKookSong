package me.realseek.command.qqmuisc;

import me.realseek.Main;
import me.realseek.api.QQMusicAPI;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;

public class QQMusicCookie implements UserCommandExecutor {

    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        // 重载配置文件
        Main.getInstance().reloadConfig();
        // 调用 cookie
        QQMusicAPI.qqMusicCookie();
        // 无论是否成功都返回
        message.reply("已刷新");
    }
}
