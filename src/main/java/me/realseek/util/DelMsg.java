package me.realseek.util;

import me.realseek.Main;
import snw.jkook.message.Message;

public class DelMsg {
    public static void delMsg(Message message){
        Main.setBotMessageUUID(message.reply("已添加"));
        // 删除 bot 回复
        Main.getInstance().getCore().getUnsafe().getTextChannelMessage(Main.getBotMessageUUID()).delete();
        // 删除用户消息
        message.delete();
    }
}
