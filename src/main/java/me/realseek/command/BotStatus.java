package me.realseek.command;

import me.realseek.util.JudgeBotInVoice;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;

public class BotStatus implements UserCommandExecutor {
    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        if (JudgeBotInVoice.status(sender,arguments,message) == false){
            message.reply("当前无人使用Bot");
        }else {
            message.reply("当前有人正在使用Bot");
        }
    }
}
