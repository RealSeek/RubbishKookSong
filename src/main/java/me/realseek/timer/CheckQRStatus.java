package me.realseek.timer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.realseek.Main;
import me.realseek.command.netease.NeteaseLogin;
import me.realseek.pojo.Netease;
import me.realseek.util.TimeStamp;
import me.realseek.util.apimethod.NeteaseMethod;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import snw.jkook.message.TextChannelMessage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class CheckQRStatus {
    public static void checkStatus(){
        Timer timer = new Timer();
        TextChannelMessage loginMsg = Main.getInstance().getCore().getUnsafe().getTextChannelMessage(NeteaseLogin.getLoginMsg());
        // 安排一个任务在指定时间之后执行，并在 60 秒后停止
        // 安排一个任务按固定间隔时间执行
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 获取扫码状态
                int status = NeteaseMethod.CheckQRStatus();
                if (status == 803){
                    // 登录成功则取消检测
                    loginMsg.delete();
                    System.out.println("登录成功");
                    timer.cancel();
                }
            }
        }, 0, 2000);
        // 在 60 秒后停止定时任务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                loginMsg.delete();
                timer.cancel();
            }
        }, 60000);
    }
}

