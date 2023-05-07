package me.realseek.command;

import me.realseek.voice.JoinVoice;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;

public class BotStatus implements UserCommandExecutor {
    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        if (JoinVoice.getWebSocket() != null){
            message.reply("当前正有人使用Bot");
        }else {
            message.reply("当前没有人使用Bot");
        }
    }
}
