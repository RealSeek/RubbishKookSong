package me.realseek.util;

import cn.hutool.http.HttpUtil;
import me.realseek.Main;
import me.realseek.api.NeteaseAPI;
import me.realseek.pojo.Bilibili;
import me.realseek.pojo.Netease;
import me.realseek.pojo.QQMusic;
import snw.jkook.HttpAPI;
import snw.jkook.entity.abilities.Accessory;
import snw.jkook.message.component.card.*;
import snw.jkook.message.component.card.element.ButtonElement;
import snw.jkook.message.component.card.element.ImageElement;
import snw.jkook.message.component.card.element.MarkdownElement;
import snw.jkook.message.component.card.element.PlainTextElement;
import snw.jkook.message.component.card.module.ActionGroupModule;
import snw.jkook.message.component.card.module.ContextModule;
import snw.jkook.message.component.card.module.SectionModule;

import java.util.Arrays;

public class Card {
    static HttpAPI httpAPI = Main.getInstance().getCore().getHttpAPI();

    // 构建播放卡片消息
    public static MultipleCardComponent playCard() {
        boolean fist = true;
        int num = 0;

        CardBuilder cardBuilder = new CardBuilder();

        cardBuilder.setTheme(Theme.INFO).setSize(Size.LG);

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("**当前正在播放：**\n---"), null, Accessory.Mode.RIGHT
        ));

        Object musicOBJ = null;
        if (!Main.getMusicList().isEmpty()) {
            musicOBJ = Main.getMusicList().getFirst();
        }

        // 歌名
        String musicName = null;
        // 歌手
        String singer = null;
        // 封面 url
        String pic = null;

        if (musicOBJ instanceof QQMusic) {
            QQMusic qqMusic = (QQMusic) musicOBJ;
            String qqName = qqMusic.getName().replace("*", "\\*").replace("`", "\\`");
            String qqSinger = qqMusic.getArtName().replace("*", "\\*").replace("`", "\\`");
            String qSender = qqMusic.getSender().getName().replace("*", "\\*").replace("`", "\\`");
            cardBuilder.addModule(new SectionModule(
                    new MarkdownElement("  **歌名:  " + qqName + "**\n" +
                            "  **歌手:  " + qqSinger + "**\n" +
                            "  **音源:  QQ音乐**\n" +
                            "  **用户:  (font)" + qSender + "(font)[pink]**\n"),
                    new ImageElement(httpAPI.uploadFile("pic", qqMusic.getPicUrl()), null, Size.SM, false),
                    Accessory.Mode.RIGHT
            ));
        } else if (musicOBJ instanceof Bilibili) {
            Bilibili bilibili = (Bilibili) musicOBJ;
            String biliName = bilibili.getTitle().replace("*", "\\*").replace("`", "\\`");
            String biliUP = bilibili.getUpName().replace("*", "\\*").replace("`", "\\`");
            String biliSender = bilibili.getSender().getName().replace("*", "\\*").replace("`", "\\`");
            cardBuilder.addModule(new SectionModule(
                    new MarkdownElement("  **标题:  " + biliName + "**\n" +
                            "  **作者:  " + biliUP + "**\n" +
                            "  **音源:  B站**\n" +
                            "  **用户:  (font)" + biliSender + "(font)[pink]**\n"),
                    new ImageElement(httpAPI.uploadFile("pic", bilibili.getPic()), null, Size.SM, false),
                    Accessory.Mode.RIGHT
            ));
        } else {
            Netease netease = (Netease) musicOBJ;
            String nName = netease.getName().replace("*", "\\*").replace("`", "\\`");
            String nSinger = netease.getArtName().replace("*", "\\*").replace("`", "\\`");
            String nSender = netease.getSender().getName().replace("*", "\\*").replace("`", "\\`");
            cardBuilder.addModule(new SectionModule(
                    new MarkdownElement("  **歌名:  " + nName + "**\n" +
                            "  **歌手:  " + nSinger + "**\n" +
                            "  **音源:  网易云**\n" +
                            "  **用户:  (font)" + nSender + "(font)[pink]**\n"),
                    new ImageElement(httpAPI.uploadFile("pic", netease.getMusicPicUrl()), null, Size.SM, false),
                    Accessory.Mode.RIGHT
            ));
        }

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("---"), null, Accessory.Mode.RIGHT
        ));

        cardBuilder.addModule(
                new ActionGroupModule(
                        Arrays.asList(
                                new ButtonElement(Theme.PRIMARY,
                                        "nextMusic",
                                        ButtonElement.EventType.RETURN_VAL,
                                        new PlainTextElement("下一首歌")),
                                new ButtonElement(Theme.DANGER,
                                        "stop",
                                        ButtonElement.EventType.RETURN_VAL,
                                        new PlainTextElement("停止所有"))
                        )
                )
        );

        // 再 new 一个卡片
        cardBuilder.newCard().setTheme(Theme.INFO).setSize(Size.LG);

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("**播放队列：**\n---"), null, Accessory.Mode.RIGHT
        ));

        for (Object object : Main.getMusicList()) {
            if (Main.getMusicList().size() == 1){
                cardBuilder.addModule(new SectionModule(
                        new PlainTextElement(
                                "队列内无其他歌曲"
                        ),
                        null,
                        null
                ));
                cardBuilder.addModule(new SectionModule(
                        new MarkdownElement("---"), null, Accessory.Mode.RIGHT
                ));
                break;
            }else {
                if (fist){
                    fist = false;
                    continue;
                }
                // 遍历 2-6 个内容
                if (object instanceof QQMusic) {
                    QQMusic qqMusic = (QQMusic) object;
                    musicName = qqMusic.getName() + " - " + qqMusic.getArtName();
                    pic = qqMusic.getPicUrl();
                    // 渲染卡片
                    cardBuilder.addModule(new SectionModule(
                            new MarkdownElement("  **" + musicName + "**"),
                            new ImageElement(httpAPI.uploadFile("pic", pic),
                                    null, false),
                            Accessory.Mode.LEFT
                    ));
                    cardBuilder.addModule(new ContextModule(
                            Arrays.asList(
                                    new PlainTextElement("\n源: QQ音乐"),
                                    new ImageElement("https://img.kookapp.cn/assets/2023-05/KapvPXiQe800w00w.png", null, false),
                                    new PlainTextElement("  |  用户: " + qqMusic.getSender().getName()),
                                    new ImageElement(qqMusic.getSender().getAvatarUrl(false), null, true)
                            )
                    ));
                } else if (object instanceof Bilibili) {
                    Bilibili bilibili = (Bilibili) object;
                    musicName = bilibili.getTitle();
                    pic = bilibili.getPic();
                    // 渲染卡片
                    cardBuilder.addModule(new SectionModule(
                            new MarkdownElement("  **" + musicName + "**"),
                            new ImageElement(httpAPI.uploadFile("pic", pic),
                                    null, false),
                            Accessory.Mode.LEFT
                    ));
                    cardBuilder.addModule(new ContextModule(
                            Arrays.asList(
                                    new PlainTextElement("\n源: B站"),
                                    new ImageElement("https://img.kookapp.cn/assets/2023-05/r4WyrVwPho00w00w.png", null, false),
                                    new PlainTextElement("  |  用户: " + bilibili.getSender().getName()),
                                    new ImageElement(bilibili.getSender().getAvatarUrl(false), null, true)
                            )
                    ));
                } else {
                    Netease netease = (Netease) object;
                    musicName = netease.getName() + " - " + netease.getArtName();
                    pic = netease.getMusicPicUrl();
                    // 渲染卡片
                    cardBuilder.addModule(new SectionModule(
                            new MarkdownElement("  **" + musicName + "**"),
                            new ImageElement(httpAPI.uploadFile("pic", pic),
                                    null, false),
                            Accessory.Mode.LEFT
                    ));
                    cardBuilder.addModule(new ContextModule(
                            Arrays.asList(
                                    new PlainTextElement("\n源: 网易云"),
                                    new ImageElement("https://img.kookapp.cn/assets/2023-05/hULgrDPVq200w00w.png", null, false),
                                    new PlainTextElement("  |  用户: " + netease.getSender().getName()),
                                    new ImageElement(netease.getSender().getAvatarUrl(false), null, true)
                            )
                    ));
                }
                cardBuilder.addModule(new SectionModule(
                        new MarkdownElement("---"), null, Accessory.Mode.RIGHT
                ));

                num++;
                if (num == 5){
                    break;
                }
            }
        }

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("**共有" + Main.getMusicList().size() + "首歌曲**"), null, Accessory.Mode.RIGHT
        ));

        return cardBuilder.build();
    }

    // 无歌曲播放
    // 构建播放卡片消息
    public static MultipleCardComponent noPlayCard() {
        CardBuilder cardBuilder = new CardBuilder();
        cardBuilder.setTheme(Theme.INFO).setSize(Size.LG);

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("**当前正在播放：**\n---"), null, Accessory.Mode.RIGHT
        ));

        cardBuilder.addModule(new SectionModule(
                new PlainTextElement("暂无歌曲"),
                new ImageElement(httpAPI.uploadFile("pic", "https://img.kookapp.cn/assets/2023-04/JDhBg2PbD30a70ag.gif"), null, false),
                Accessory.Mode.LEFT
        ));

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("---"), null, Accessory.Mode.RIGHT
        ));

        // 再 new 一个卡片
        cardBuilder.newCard().setTheme(Theme.INFO).setSize(Size.LG);

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("**播放队列：**\n---"), null, Accessory.Mode.RIGHT
        ));

        cardBuilder.addModule(new SectionModule(
                new PlainTextElement(
                        "暂时还没有歌曲呢"
                ),
                null,
                null
        ));

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("---\n**共有" + 0 + "首歌曲**"), null, Accessory.Mode.RIGHT
        ));

        return cardBuilder.build();
    }

    // 帮助文本卡片消息（已弃用）
    public static MultipleCardComponent helpCard() {
        CardBuilder cardBuilder = new CardBuilder();
        cardBuilder.setTheme(Theme.INFO).setSize(Size.LG);
        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("---\n" +
                        "**(font)网易云音乐(font)[success]**\n" +
                        "/网易登录 : 二维码登录网易云\n\n" +
                        "/网易 歌名 : 点网易云音乐的歌曲\n例如：\n`/网易 你瞒我瞒`\n\n" +
                        "/网易歌单 url : 导入歌单内全部的歌曲\n导入过程最好不要进行任何操作以免出现问题 \n例如: \n`/网易歌单 https://music.163.com/playlist?id=10602549&userid=67607919`\n\n"), null, Accessory.Mode.RIGHT
        ));
        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("---\n" +
                        "**(font)QQ音乐(font)[success]**\n" +
                        "/qq刷新 : 将配置文件内的qq音乐cookie导入到api内\n`注意cookie只有一天有效期 过期后更改配置文件内的cookie后直接输入此指令即可`\n\n" +
                        "/qq 歌名 : 点QQ音乐平台的歌曲\n例如：\n`/QQ 你瞒我瞒`\n\n"), null, Accessory.Mode.RIGHT
        ));
        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("---\n" +
                        "**(font)Bilibili(font)[success]**\n" +
                        "/bili url : 播放B站视频链接（支持分P视频）\n例如: \n`/bili https://www.bilibili.com/video/BV1Cr4y1J7gV/?spm_id_from=333.337.search-card.all.click&vd_source=42b284ed4a8ece0b11bc6f7e22519416`\n\n" +
                        "---"), null, Accessory.Mode.RIGHT
        ));
        return cardBuilder.build();
    }
}
