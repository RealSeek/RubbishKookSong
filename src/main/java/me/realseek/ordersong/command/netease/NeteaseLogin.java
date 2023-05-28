package me.realseek.ordersong.command.netease;

import me.realseek.ordersong.Main;
import me.realseek.ordersong.api.NeteaseAPI;
import me.realseek.ordersong.util.QRCodeUtil;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.jkook.message.component.FileComponent;

import java.io.File;

public class NeteaseLogin implements UserCommandExecutor {
    /**
     * 二维码的消息uuid
     */
    static String loginMsg;

    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        // 生成二维码保存到 config
        QRCodeUtil.createQRCode(NeteaseAPI.neteaseLogin());
        // 上传二维码
        String QRCodeUrl = Main.getInstance().getCore().getHttpAPI().uploadFile(new File(Main.getResPath() + "\\QRCode.jpg"));
        // new 一个图片类型用于发送
        FileComponent loginImage = new FileComponent(QRCodeUrl,"二维码", 200, FileComponent.Type.IMAGE);

        loginMsg = message.reply(loginImage);
    }

    /**
     * 获取二维码消息的 uuid
     * @return
     */
    public static String getLoginMsg() {
        return loginMsg;
    }
}
