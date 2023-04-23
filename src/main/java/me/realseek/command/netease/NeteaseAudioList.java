package me.realseek.command.netease;

import me.realseek.Main;
import me.realseek.ffmpeg.PlayMusic;
import me.realseek.pojo.Netease;
import me.realseek.timer.ProcessStatus;
import me.realseek.util.*;
import me.realseek.util.apimethod.NeteaseMethod;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.jkook.message.TextChannelMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NeteaseAudioList implements UserCommandExecutor {
    static Netease netease = Main.getNetease();

    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        try {
            // 判断 sender 是否在语音频道内
            if (InChannel.inChannel(sender, arguments, message)) {
                System.out.println("进入网易云歌单点歌");
                // 传出Message
                Main.setMessage(message);
                // 获取处理后的点歌参数
                String parameters = MessageUtil.getFullMessage(sender, arguments, message);
                Pattern playList = Pattern.compile("playlist\\?id=(\\d+)");
                Matcher playListId = playList.matcher(parameters);
                // 如果找到歌单ID
                if (playListId.find()) {
                    String playlistId = playListId.group(1);
                    TextChannelMessage rp = Main.getInstance().getCore().getUnsafe().getTextChannelMessage(message.reply("正在检索歌单内的歌曲请稍等"));
                    // 遍历歌单歌曲并添加到播放内
                    NeteaseMethod.getMusicListInfo(playlistId);
                    // 删除
                    rp.delete();
                    // 判断 Bot 状态
                    if (JudgeBotInVoice.status(sender, arguments, message)) {
                        // Bot在语音内
                        // 删除”已添加“
                        DelMsg.delMsg(message);
                        // 更新卡片
                        PlayMusic.getBotMessage1().delete();
                        PlayMusic.getBotMessage2().delete();
                        PlayMusic.setMsgMusicNow(Main.getMessage().sendToSource(Card.playCard()));
                        PlayMusic.setMsgMuiscList(Main.getMessage().sendToSource(Card.playList()));
                        PlayMusic.setBotMessage1(Main.getInstance().getCore().getUnsafe().getTextChannelMessage(PlayMusic.getMsgMusicNow()));
                        PlayMusic.setBotMessage2(Main.getInstance().getCore().getUnsafe().getTextChannelMessage(PlayMusic.getMsgMuiscList()));
                        // 设置状态
                        PlayMusic.setFist(false);
                    } else {
                        // Bot不在语音内
                        // 加入语音
                        JoinChannel.joinChannel(sender, arguments, message);
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
                    }
                } else {
                    message.reply("你给的参数似乎不是歌单链接");
                }
            } else {
                message.reply("你当前似乎不在语音频道内");
            }
        }catch (Exception e){
            message.reply("请确保你的歌单为公开歌单（红心歌单也不支持）");
        }
    }
}

