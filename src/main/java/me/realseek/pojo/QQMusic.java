package me.realseek.pojo;

import snw.jkook.entity.User;

public class QQMusic {
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
