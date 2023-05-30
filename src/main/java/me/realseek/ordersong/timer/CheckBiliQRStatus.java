package me.realseek.ordersong.timer;

import me.realseek.ordersong.Main;
import me.realseek.ordersong.command.netease.NeteaseLogin;
import me.realseek.ordersong.util.apimethod.BilibiliMethod;
import snw.jkook.message.Message;
import snw.jkook.message.TextChannelMessage;

import java.util.Timer;
import java.util.TimerTask;

public class CheckBiliQRStatus {
    public static void checkStatus(Message message){
        Timer timer = new Timer();
        TextChannelMessage loginMsg = Main.getInstance().getCore().getHttpAPI().getTextChannelMessage(Main.getLoginMsg());
        // 安排一个任务在指定时间之后执行，并在 60 秒后停止
        // 安排一个任务按固定间隔时间执行
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 获取扫码状态
                int status = BilibiliMethod.CheckQRStatus();
                if (status == 0){
                    // 登录成功则取消检测
                    // 删除二维码信息
                    loginMsg.delete();
                    message.delete();
                    System.out.println("B站登录成功");
                    // 关闭计时器
                    timer.cancel();
                }
            }
        }, 0, 2000);
        // 在 60 秒后停止定时任务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 删除二维码
                loginMsg.delete();
                message.delete();
                // 关闭计时器
                timer.cancel();
            }
        }, 60000);
    }
}

