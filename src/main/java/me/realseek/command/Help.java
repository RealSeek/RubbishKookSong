package me.realseek.command;

import me.realseek.Main;
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
        File menu = new File(Main.getMenuPath());
        FileComponent fileComponent = new FileComponent(Main.getInstance().getCore().getHttpAPI().uploadFile(menu), "", 3000, FileComponent.Type.IMAGE);
        message.reply(fileComponent);
    }
}
