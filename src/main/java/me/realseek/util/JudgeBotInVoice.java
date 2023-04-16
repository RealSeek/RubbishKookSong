package me.realseek.util;

import me.realseek.Main;
import org.jetbrains.annotations.Nullable;
import snw.jkook.entity.Guild;
import snw.jkook.entity.User;
import snw.jkook.entity.channel.VoiceChannel;
import snw.jkook.message.Message;
import snw.jkook.message.TextChannelMessage;
import snw.jkook.util.PageIterator;

import java.util.Collection;

public class JudgeBotInVoice {
    static String userVoidChannel;
    static String botVoidChannel;

    public static Boolean status(User sender, Object[] arguments, @Nullable Message message) {
        Boolean temp = null;
        Guild guild = ((TextChannelMessage) message).getChannel().getGuild();
        PageIterator<Collection<VoiceChannel>> userJoinedVoiceChannel = sender.getJoinedVoiceChannel(guild);
        // 判断 User 是否在语音频道内
        if (userJoinedVoiceChannel.hasNext()) {
            Collection<VoiceChannel> next = userJoinedVoiceChannel.next();
            for (VoiceChannel voiceChannel : next) {
                // 获取 sender 的语音频道id
                userVoidChannel = voiceChannel.getId();
                System.out.println("以获取用户频道");
            }
        }

        PageIterator<Collection<VoiceChannel>> botJoinedVoiceChannel = Main.getInstance().getCore().getUser().getJoinedVoiceChannel(guild);
        // 判断 Bot 是否在语音频道内
        if (botJoinedVoiceChannel.hasNext()) {
            Collection<VoiceChannel> next = botJoinedVoiceChannel.next();
            for (VoiceChannel voiceChannel : next) {
                // 获取 Bot 的语音频道id
                botVoidChannel = voiceChannel.getId();
                if (userVoidChannel == botVoidChannel) {
                    // System.out.println("进入if判断-在同一个语音");
                    temp = true;
                } else {
                    // BOT 在语音  但不在 sender 的语音
                    return null;
                }
            }
        }else {
            // 若Bot未在任何语音
            temp = false;
        }
        return temp;
    }
}
