package me.realseek.util;

import me.realseek.Main;
import snw.jkook.HttpAPI;
import snw.jkook.entity.abilities.Accessory;
import snw.jkook.message.component.card.CardBuilder;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.jkook.message.component.card.Size;
import snw.jkook.message.component.card.Theme;
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
        return cardBuilder.build();
    }

    // 构建播放队列卡片消息
    public static MultipleCardComponent playList() {
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
        CardBuilder cardBuilder = new CardBuilder();
        cardBuilder.setTheme(Theme.INFO).setSize(Size.LG);

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

        return cardBuilder.build();
    }

    // 构建播放队列卡片消息
    public static MultipleCardComponent noPlayList() {
        CardBuilder cardBuilder = new CardBuilder();
        cardBuilder.setTheme(Theme.INFO).setSize(Size.LG);

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
}
