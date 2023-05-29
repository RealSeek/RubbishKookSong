package me.realseek.ordersong.command.bilibili;

import me.realseek.ordersong.Main;
import me.realseek.ordersong.api.BilibiliAPI;
import me.realseek.ordersong.ffmpeg.PlayMusic;
import me.realseek.ordersong.pojo.Bilibili;
import me.realseek.ordersong.timer.ProcessStatus;
import me.realseek.ordersong.util.*;
import me.realseek.ordersong.util.apimethod.BilibiliMethod;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;

public class BilibiliAudio implements UserCommandExecutor {
    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        if (InChannel.inChannel(sender,arguments,message)) {
            try {
                // System.out.println("进入B站点歌");

                // 传出Message
                Main.setMessage(message);

                // 获取处理后的点歌参数
                String parameters = MessageUtil.getFullMessage(sender, arguments, message);

                // 第一步 获取B站视频信息
                // System.out.println(parameters);

                // 处理参数
                BilibiliMethod.setBiliBVNumAndPnum(parameters);

                // 获取信息
                BilibiliAPI.biliVideoInfo(Main.getBilibili().getBvNum());

                // 第二步 获取音频流
                String musicDownloadUrl = BilibiliAPI.biliVideo(Main.getBilibili().getBvNum(), Main.getBilibili().getCid());

                // new 一个对象 并把数据传入
                Bilibili bilibili = new Bilibili(Main.getBilibili(), sender);

                // 将对象存进集合
                Main.getMusicList().add(bilibili);

                // 删除 sender 消息和 "已添加"
                DelMsg.delMsg(message);

                // 如果音频流链接为 null
                if (musicDownloadUrl == null) {
                    message.reply("似乎找不到这个视频捏");
                    // 若不是 则进入正常流程
                } else {
                    // 判断Bot状态
                    if (JudgeBotInVoice.status(sender, arguments, message) == true) {
                        // Bot在语音内
                        // 设置状态
                        PlayMusic.setFist(false);
                        // 更新卡片
                        // 判断播放状态
                        if (Main.getPlayStatus()) {
                            // 删除旧的消息
                            PlayMusic.getBotMessage().delete();
                            // 发送新的消息卡片 并且拿消息 uuid
                            PlayMusic.setMsgMusicUUID(message.sendToSource(Card.playCard()));
                            // 通过 uuid 拿到消息对象
                            PlayMusic.setBotMessage(Main.getInstance().getCore().getHttpAPI().getTextChannelMessage(PlayMusic.getMsgMusicUUID()));
                        }
                        // 由于在语音内不需要启动计时器，计时器内有检测歌单数量的方法进行播放
                    } else if (JudgeBotInVoice.status(sender, arguments, message) == false){
                        // Bot不在语音内
                        // 点歌
                        PlayMusic.setFist(true);
                        // 加入语音
                        JoinChannel.joinChannel(sender, arguments, message);
                        // 开启计时器 进入计时器内播放
                        Main.setProcessStatus(new ProcessStatus());
                        Main.getProcessStatus().start();
                    }else if (JudgeBotInVoice.status(sender, arguments, message) == null){
                        message.reply("抱歉，当前Bot正在别的频道放歌，暂时不能使用这个Bot");
                    }
                }
            } catch (Exception e) {
                System.out.println("\n已将错误输出");
                throw new RuntimeException(e);
            }
        }else {
            message.reply("你当前似乎不在语音频道内");
        }
    }
}
