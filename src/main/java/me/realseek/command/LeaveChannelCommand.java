package me.realseek.command;

import me.realseek.Main;
import me.realseek.ffmpeg.FFmpeg;
import me.realseek.ffmpeg.PlayMusic;
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
                    // 断开语音
                    JoinVoice.disconnect();
                    // 关闭检测
                    Main.getProcessStatus().close();
                    // 拿消息
                    String msgl = message.reply("已断开语音频道");
                    TextChannelMessage msg = Main.getInstance().getCore().getUnsafe().getTextChannelMessage(msgl);
                    // 关闭进程
                    if (FFmpeg.getZMQ().isAlive()) {
                        FFmpeg.getZMQ().destroy();
                        PlayMusic.getPlayMusicProcess().destroy();
                    }
                    // 删除消息
                    if (PlayMusic.getBotMessage() != null) {
                        PlayMusic.getBotMessage().delete();
                    }
                    // 清空所有
                    // 标题
                    Main.getMusicTitleList().clear();
                    // 封面
                    Main.getMusicPicList().clear();
                    // 歌曲ID
                    if (!Main.getMusicIdList().isEmpty()) {
                        Main.getMusicIdList().remove(0);
                    }
                    msg.delete();
                    message.delete();
                } else {
                    message.reply("我当前并不在语音频道内");
                }
            }
        }
    }
}
