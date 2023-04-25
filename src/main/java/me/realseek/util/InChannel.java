package me.realseek.util;

import me.realseek.Main;
import me.realseek.voice.JoinVoice;
import snw.jkook.entity.Guild;
import snw.jkook.entity.User;
import snw.jkook.entity.channel.VoiceChannel;
import snw.jkook.message.Message;
import snw.jkook.message.TextChannelMessage;
import snw.jkook.util.PageIterator;

import java.util.Collection;

public class InChannel {
    /**
     * 判断 sender 是否在语音内
     * @param sender
     * @param arguments
     * @param message
     * @return
     */
    public static Boolean inChannel(User sender, Object[] arguments, Message message){
        String VoidChannel = null;
        // 获取服务器的 guild
        Guild guild = ((TextChannelMessage) message).getChannel().getGuild();
        PageIterator<Collection<VoiceChannel>> joinedVoiceChannel = sender.getJoinedVoiceChannel(guild);
        // 判断 User 是否在语音频道内
        if (joinedVoiceChannel.hasNext()){
            Collection<VoiceChannel> next = joinedVoiceChannel.next();
            for (VoiceChannel voiceChannel: next){
                VoidChannel = voiceChannel.getId();
            }
            // 在语音内
            return true;
        }else {
            // 不在语音内
            return false;
        }
    }
}
