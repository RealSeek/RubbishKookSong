package me.realseek.command.qqmuisc;

import me.realseek.Main;
import me.realseek.api.NeteaseAPI;
import me.realseek.api.QQMusicAPI;
import me.realseek.ffmpeg.PlayMusic;
import me.realseek.pojo.QQMusic;
import me.realseek.timer.ProcessStatus;
import me.realseek.util.*;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;

public class QQMusicAudio implements UserCommandExecutor {
    static QQMusic qqMusic = Main.getQqMusic();
    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        if (InChannel.inChannel(sender, arguments, message)) {
            try {
                System.out.println("进入QQ音乐点歌");
                // 传出Message
                Main.setMessage(message);
                // 获取处理后的点歌参数
                String parameters = MessageUtil.getFullMessage(sender, arguments, message);
                // 获取歌曲信息
                QQMusicAPI.qqMusicSearch(parameters);
                // 获取歌曲下载链接
                String musicDownloadUrl = QQMusicAPI.qqMusicDownloadUrl(qqMusic.getSongmid());
                if (musicDownloadUrl.equals("cookie过期或无版权")) {
                    System.out.println("cookie过期或无版权");
                    message.reply("cookie过期或无版权");
                } else {
                    // 判断Bot状态
                    if (JudgeBotInVoice.status(sender, arguments, message) == true) {
                        // Bot在语音内
                        // 添加播放列表
                        Main.getMusicTitleList().add("QQ音乐：" + qqMusic.getName() + " - " + qqMusic.getArtName());
                        Main.getMusicPicList().add(qqMusic.getPicUrl());
                        // 删除”已添加“
                        DelMsg.delMsg(message);
                        // 判断播放状态
                        // 更新卡片
                        if (Main.getPlayStatus()) {
                            PlayMusic.getBotMessage().delete();
                            PlayMusic.setMsgMusicNow(Main.getMessage().sendToSource(Card.playCard()));
                            PlayMusic.setBotMessage(Main.getInstance().getCore().getUnsafe().getTextChannelMessage(PlayMusic.getMsgMusicNow()));
                        }
                        // 设置状态
                        PlayMusic.setFist(false);
                        // 由于在语音内不需要启动计时器，计时器内有检测歌单数量的方法进行播放
                    } else if (JudgeBotInVoice.status(sender, arguments, message) == false){
                        // Bot不在语音内
                        // 加入语音
                        JoinChannel.joinChannel(sender, arguments, message);
                        // 添加播放列表
                        Main.getMusicTitleList().add("QQ音乐：" + qqMusic.getName() + " - " + qqMusic.getArtName());
                        Main.getMusicPicList().add(qqMusic.getPicUrl());
                        // 设置UUID
                        Main.setMsgMuiscNoPlay(message.reply("已添加"));
                        Main.setBotMessageNoPlay(Main.getInstance().getCore().getUnsafe().getTextChannelMessage(Main.getMsgMuiscNoPlay()));
                        // 删除消息
                        message.delete();
                        // 点歌
                        PlayMusic.setFist(true);
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
