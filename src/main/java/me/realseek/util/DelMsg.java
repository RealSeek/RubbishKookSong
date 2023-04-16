package me.realseek.util;

import me.realseek.Main;
import snw.jkook.message.Message;

public class DelMsg {
    public static void delMsg(Message message){
        Main.setMsgMuiscInPlay(message.reply("已添加"));
        Main.getMsgList().add(Main.getMsgMuiscInPlay());
        Thread delMsg = new Thread(() -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 删除 bot 回复
            for (String uuid : Main.getMsgList()){
                // botMessageInPlay = Main.getInstance().getCore().getUnsafe().getTextChannelMessage(uuid);
                Main.setBotMessageInPlay(Main.getInstance().getCore().getUnsafe().getTextChannelMessage(uuid));
                Main.getBotMessageInPlay().delete();
            };
            // 删除用户消息
            message.delete();
        });
        delMsg.start();
    }
}
