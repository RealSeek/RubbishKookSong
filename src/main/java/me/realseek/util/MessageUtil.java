package me.realseek.util;

import me.realseek.pojo.Bilibili;
import snw.jkook.entity.User;
import snw.jkook.message.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {
    Bilibili bilibili = new Bilibili();
    public static String getFullMessage(User sender, Object[] arguments, Message message){
        String str = "";
        if (arguments.length == 0) {
            message.reply("请带上你要点的歌名");
        } else for (Object argument : arguments) {
            str += argument + " ";
        }
        return str.substring(0,str.length()-1);
    }
}
