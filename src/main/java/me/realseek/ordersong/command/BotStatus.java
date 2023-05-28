package me.realseek.ordersong.command;

import me.realseek.ordersong.ffmpeg.FFmpeg;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;

public class BotStatus implements UserCommandExecutor {
    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        try {
            if (FFmpeg.getZMQ().isAlive()){
                message.reply("当前正有人使用Bot");
            }else {
                message.reply("当前没有人使用Bot");
            }
        }catch (NullPointerException e){
            // 未初始化 ZMQ 进程
            message.reply("当前没有人使用Bot");
        }
    }
}
