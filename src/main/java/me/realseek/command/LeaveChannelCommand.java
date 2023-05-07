package me.realseek.command;

import me.realseek.Main;
import me.realseek.ffmpeg.FFmpeg;
import me.realseek.ffmpeg.PlayMusic;
import me.realseek.util.StopAll;
import me.realseek.voice.JoinVoice;
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
                    TextChannelMessage msg = Main.getInstance().getCore().getUnsafe().getTextChannelMessage(message.reply("已断开语音频道"));
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
