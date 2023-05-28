package me.realseek.ordersong.command.netease;

import me.realseek.ordersong.Main;
import me.realseek.ordersong.ffmpeg.PlayMusic;
import me.realseek.ordersong.timer.ProcessStatus;
import me.realseek.ordersong.util.*;
import me.realseek.ordersong.util.apimethod.NeteaseMethod;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;
import snw.jkook.message.TextChannelMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NeteaseAudioList implements UserCommandExecutor {
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
                // 删除 sender 消息和 "已添加"
                // DelMsg.delMsg(message);

                // 如果找到歌单ID
                if (playListId.find()) {
                    String playlistId = playListId.group(1);
                    TextChannelMessage rp = Main.getInstance().getCore().getUnsafe().getTextChannelMessage(message.reply("正在检索歌单内的歌曲请稍等"));
                    // 遍历歌单歌曲并添加到播放内
                    // 歌单逻辑在方法内处理
                    NeteaseMethod.getMusicListInfo(playlistId, sender);
                    // 删除
                    rp.delete();
                    // 判断 Bot 状态
                    if (JudgeBotInVoice.status(sender, arguments, message)) {
                        // Bot在语音内
                        // 设置状态
                        PlayMusic.setFist(false);
                        // 更新卡片
                        if (Main.getPlayStatus()) {
                            // 删除旧的消息
                            PlayMusic.getBotMessage().delete();
                            // 发送新的消息卡片 并且拿消息 uuid
                            PlayMusic.setMsgMusicUUID(Main.getMessage().sendToSource(Card.playCard()));
                            // 通过 uuid 拿到消息对象
                            PlayMusic.setBotMessage(Main.getInstance().getCore().getHttpAPI().getTextChannelMessage(PlayMusic.getMsgMusicUUID()));
                        }
                    } else {
                        // Bot不在语音内
                        // 点歌
                        PlayMusic.setFist(true);
                        // 删除 sender 消息和 "已添加"
                        DelMsg.delMsg(message);
                        // 加入语音
                        JoinChannel.joinChannel(sender, arguments, message);
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
            System.out.println("\n已将错误输出");
            throw new RuntimeException(e);
        }
    }
}

