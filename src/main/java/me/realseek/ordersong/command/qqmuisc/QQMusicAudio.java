package me.realseek.ordersong.command.qqmuisc;

import me.realseek.ordersong.Main;
import me.realseek.ordersong.api.QQMusicAPI;
import me.realseek.ordersong.ffmpeg.PlayMusic;
import me.realseek.ordersong.pojo.QQMusic;
import me.realseek.ordersong.timer.ProcessStatus;
import me.realseek.ordersong.util.*;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;

public class QQMusicAudio implements UserCommandExecutor {
    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        if (InChannel.inChannel(sender, arguments, message)) {
            try {
                // System.out.println("进入QQ音乐点歌");
                // 传出Message
                Main.setMessage(message);
                // 获取处理后的点歌参数
                String parameters = MessageUtil.getFullMessage(sender, arguments, message);
                // 获取歌曲信息
                QQMusicAPI.qqMusicSearch(parameters);
                // 获取歌曲下载链接
                String musicDownloadUrl = QQMusicAPI.qqMusicDownloadUrl(Main.getQqMusic().getSongmid());

                // new 一个对象 并把数据传入
                QQMusic qqMusic = new QQMusic(Main.getQqMusic(), sender);

                // 将对象存进集合
                Main.getMusicList().add(qqMusic);

                // 删除 sender 消息和 "已添加"
                DelMsg.delMsg(message);

                if (musicDownloadUrl.equals("cookie过期或无版权")) {
                    System.out.println("cookie过期或无版权");
                    message.reply("cookie过期或无版权");
                } else {
                    // 判断Bot状态
                    if (JudgeBotInVoice.status(sender, arguments, message) == true) {
                        // Bot在语音内
                        // 设置状态
                        PlayMusic.setFist(false);
                        // 判断播放状态
                        // 更新卡片
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
                    } else if (JudgeBotInVoice.status(sender, arguments, message) != null){
                        message.reply("抱歉，当前Bot正在别的频道放歌，暂时不能使用这个Bot");
                    }
                }
            } catch (NullPointerException e) {
                message.reply("未获取到音乐，请检查QQ音乐Cookie是否过期或歌曲不存在");
            } catch (IndexOutOfBoundsException e) {
                message.reply("接口搜索不到歌曲信息，这通常是API的问题，请重试");
            }
        } else {
            message.reply("你当前似乎不在语音频道内");
        }
    }


}
