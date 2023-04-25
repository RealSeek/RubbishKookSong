package me.realseek.util;

import me.realseek.Main;
import snw.jkook.HttpAPI;
import snw.jkook.entity.abilities.Accessory;
import snw.jkook.message.component.card.*;
import snw.jkook.message.component.card.element.ButtonElement;
import snw.jkook.message.component.card.element.ImageElement;
import snw.jkook.message.component.card.element.MarkdownElement;
import snw.jkook.message.component.card.element.PlainTextElement;
import snw.jkook.message.component.card.module.ActionGroupModule;
import snw.jkook.message.component.card.module.SectionModule;

import java.util.Arrays;

public class Card {
    static HttpAPI httpAPI = Main.getInstance().getCore().getHttpAPI();

    // 构建播放卡片消息
    public static MultipleCardComponent playCard() {
        CardBuilder cardBuilder = new CardBuilder();

        cardBuilder.setTheme(Theme.INFO).setSize(Size.LG);

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("**当前正在播放：**\n---"),null,Accessory.Mode.RIGHT
        ));

        cardBuilder.addModule(new SectionModule(
                new PlainTextElement(" " + Main.getMusicTitleList().get(0)),
                new ImageElement(httpAPI.uploadFile("pic", Main.getMusicPicList().get(0)), null, false),
                Accessory.Mode.LEFT
        ));

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("---"),null,Accessory.Mode.RIGHT
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

        int musicNumber = 0;
        String musicList = "";
        for (String element : Main.getMusicTitleList()) {
            musicList += element + "\n";
            musicNumber++;
            if (musicNumber > 20){
                System.out.println("为了不过长，对卡片进行阉割");
                musicList += "......\n";
                break;
            }
        }

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("**播放队列：**\n---"),null,Accessory.Mode.RIGHT
        ));

        cardBuilder.addModule(new SectionModule(
                new PlainTextElement(
                        musicList
                ),
                null,
                null
        ));

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("---\n**共有" + Main.getMusicTitleList().size() + "首歌曲**"),null,Accessory.Mode.RIGHT
        ));

        return cardBuilder.build();
    }

    // 无歌曲播放
    // 构建播放卡片消息
    public static MultipleCardComponent noPlayCard() {
        CardBuilder cardBuilder = new CardBuilder();
        cardBuilder.setTheme(Theme.INFO).setSize(Size.LG);

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("**当前正在播放：**\n---"),null,Accessory.Mode.RIGHT
        ));

        cardBuilder.addModule(new SectionModule(
                new PlainTextElement("暂无歌曲"),
                new ImageElement(httpAPI.uploadFile("pic", "https://img.kookapp.cn/assets/2023-04/JDhBg2PbD30a70ag.gif"), null, false),
                Accessory.Mode.LEFT
        ));

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("---"),null,Accessory.Mode.RIGHT
        ));

        // 再 new 一个卡片
        cardBuilder.newCard().setTheme(Theme.INFO).setSize(Size.LG);

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("**播放队列：**\n---"),null,Accessory.Mode.RIGHT
        ));

        cardBuilder.addModule(new SectionModule(
                new PlainTextElement(
                        "暂时还没有歌曲呢"
                ),
                null,
                null
        ));

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("---\n**共有" + 0 + "首歌曲**"),null,Accessory.Mode.RIGHT
        ));

        return cardBuilder.build();
    }

    // 帮助文本卡片消息（已弃用）
    public static MultipleCardComponent helpCard(){
        CardBuilder cardBuilder = new CardBuilder();
        cardBuilder.setTheme(Theme.INFO).setSize(Size.LG);
        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("---\n" +
                        "**(font)网易云音乐(font)[success]**\n" +
                        "/网易登录 : 二维码登录网易云\n\n" +
                        "/网易 歌名 : 点网易云音乐的歌曲\n例如：\n`/网易 你瞒我瞒`\n\n" +
                        "/网易歌单 url : 导入歌单内全部的歌曲\n导入过程最好不要进行任何操作以免出现问题 \n例如: \n`/网易歌单 https://music.163.com/playlist?id=10602549&userid=67607919`\n\n"),null,Accessory.Mode.RIGHT
        ));
        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("---\n" +
                        "**(font)QQ音乐(font)[success]**\n" +
                        "/qq刷新 : 将配置文件内的qq音乐cookie导入到api内\n`注意cookie只有一天有效期 过期后更改配置文件内的cookie后直接输入此指令即可`\n\n" +
                        "/qq 歌名 : 点QQ音乐平台的歌曲\n例如：\n`/QQ 你瞒我瞒`\n\n"),null,Accessory.Mode.RIGHT
        ));
        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("---\n" +
                        "**(font)Bilibili(font)[success]**\n" +
                        "/bili url : 播放B站视频链接（支持分P视频）\n例如: \n`/bili https://www.bilibili.com/video/BV1Cr4y1J7gV/?spm_id_from=333.337.search-card.all.click&vd_source=42b284ed4a8ece0b11bc6f7e22519416`\n\n" +
                        "---"),null,Accessory.Mode.RIGHT
        ));
        return cardBuilder.build();
    }
}
