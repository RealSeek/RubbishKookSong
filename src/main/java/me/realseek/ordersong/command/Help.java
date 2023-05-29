package me.realseek.ordersong.command;

import me.realseek.ordersong.Main;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.jkook.message.component.FileComponent;

import java.io.File;

public class Help implements UserCommandExecutor {
    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        // message.reply(Card.helpCard());
        FileComponent fileComponent = new FileComponent(Main.getInstance().getCore().getHttpAPI().uploadFile("menu" ,"https://img.kookapp.cn/assets/2023-05/YIdQLnW6HJ1po2s0.png"), "", 3000, FileComponent.Type.IMAGE);
        message.reply(fileComponent);
    }
}
