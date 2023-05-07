package me.realseek.command.qqmuisc;

import me.realseek.Main;
import me.realseek.api.QQMusicAPI;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.jkook.message.TextChannelMessage;

public class QQMusicCookie implements UserCommandExecutor {

    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        // 重载配置文件
        Main.getInstance().reloadConfig();
        // 调用 cookie
        QQMusicAPI.qqMusicCookie();
        // 无论是否成功都返回
        String msgid = message.reply("已刷新");
        // 删除 sender 消息
        message.delete();
        // 删除 Bot 消息
        Main.getInstance().getCore().getUnsafe().getTextChannelMessage(msgid).delete();
    }
}
