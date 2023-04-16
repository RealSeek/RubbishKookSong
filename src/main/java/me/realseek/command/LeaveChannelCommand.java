package me.realseek.command;

import me.realseek.Main;
import me.realseek.ffmpeg.FFmpeg;
import me.realseek.ffmpeg.PlayMusic;
import me.realseek.voice.JoinVoice;
import me.realseek.voice.SimpleWebSocketListener;
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
                    JoinVoice.disconnect();
                    Main.getProcessStatus().close();
                    String msgl1 = message.reply("已断开语音频道");
                    TextChannelMessage msg1 = PlayMusic.getBotMessage1();
                    TextChannelMessage msg2 = PlayMusic.getBotMessage2();
                    TextChannelMessage msg3 = Main.getInstance().getCore().getUnsafe().getTextChannelMessage(msgl1);
                    // 关闭进程
                    if (FFmpeg.getZMQ().isAlive()) {
                        FFmpeg.getZMQ().destroy();
                        PlayMusic.getPlayMusicProcess().destroy();
                    }
                    // 删除消息
                    if (msg1 != null) {
                        msg1.delete();
                        msg2.delete();
                    }
                    // 清空所有
                    // 标题
                    Main.getMusicTitleList().clear();
                    // 封面
                    Main.getMusicPicList().clear();
                    // 歌曲链接
                    // Main.getMusicUrlList().clear();
                    // 歌曲ID
                    if (PlayMusic.getMusic().equals("网易")) {
                        Main.getMusicIdList().remove(0);
                    }
                    msg3.delete();
                    message.delete();
                } else {
                    message.reply("我当前并不在语音频道内");
                }
            }
        }
    }
}
