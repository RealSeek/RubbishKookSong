package me.realseek.ordersong.command;

import me.realseek.ordersong.Main;
import me.realseek.ordersong.util.StopAll;
import me.realseek.ordersong.voice.JoinVoice;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.jkook.message.TextChannelMessage;

public class LeaveChannelCommand implements UserCommandExecutor {
    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        // 遍历 Config 的管理员 ID
        for (String str : Main.getInstance().getConfig().getStringList("BotAdmin")) {
            // 判断发送者ID和配置文件的管理员ID是否相等
            if (sender.getId().equals(str)) {
                if (JoinVoice.getWebSocket() != null) {
                    // 拿消息
                    TextChannelMessage msg = Main.getInstance().getCore().getHttpAPI().getTextChannelMessage(message.reply("已断开语音频道"));
                    msg.delete();
                    message.delete();
                    // 关闭所有
                    StopAll.over();
                } else {
                    message.reply("我当前并不在语音频道内");
                }
            }
        }
    }
}
