package me.realseek.ordersong.command.netease;

import me.realseek.ordersong.Main;
import me.realseek.ordersong.api.BilibiliAPI;
import me.realseek.ordersong.api.NeteaseAPI;
import me.realseek.ordersong.timer.CheckBiliQRStatus;
import me.realseek.ordersong.timer.CheckNeteaseQRStatus;
import me.realseek.ordersong.util.QRCodeUtil;
import me.realseek.ordersong.util.SystemType;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.jkook.message.component.FileComponent;

import java.io.File;

public class NeteaseLogin implements UserCommandExecutor {

    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        // 生成二维码保存到 config
        QRCodeUtil.createQRCode(NeteaseAPI.neteaseLogin());
        // 上传二维码
        String QRCodeUrl = Main.getInstance().getCore().getHttpAPI().uploadFile(new File(SystemType.processFilePath(Main.getResPath() + "\\QRCode.jpg")));
        // new 一个图片类型用于发送
        FileComponent loginImage = new FileComponent(QRCodeUrl,"二维码", 200, FileComponent.Type.IMAGE);
        // 设置
        Main.setLoginMsg(message.reply(loginImage));
        // 检测登录
        CheckNeteaseQRStatus.checkStatus(message);
    }
}
