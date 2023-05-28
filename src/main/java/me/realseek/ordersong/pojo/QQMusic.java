package me.realseek.ordersong.pojo;

import snw.jkook.entity.User;

public class QQMusic {
    public QQMusic() {
    }

    public QQMusic(QQMusic qqMusic, User sender) {
        this.name = qqMusic.getName();
        this.artName = qqMusic.getArtName();
        this.picUrl = qqMusic.getPicUrl();
        this.songmid = qqMusic.getSongmid();
        this.sender = sender;
    }

    /**
     * 音乐名
     */
    private String name;
    /**
     * 歌手
     */
    private String artName;

    /**
     * 音乐封面 url
     */
    private String picUrl;

    /**
     * 歌曲ID （用于下载歌曲）
     */
    private String songmid;

    /**
     * 点歌的用户
     */
    private User sender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtName() {
        return artName;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getSongmid() {
        return songmid;
    }

    public void setSongmid(String songmid) {
        this.songmid = songmid;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
