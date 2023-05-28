package me.realseek.ordersong.pojo;

import snw.jkook.entity.User;

public class Netease {

    public Netease() {
    }

    public Netease(Netease netease, User sender) {
        this.muiscId = netease.getMuiscId();
        this.name = netease.getName();
        this.artName = netease.getArtName();
        this.musicPicUrl = netease.getMusicPicUrl();
        this.sender = sender;
    }

    /**
     * 二维码的key
     */
    private String unikey;
    /**
     * 二维码的Url
     */
    private String QRCodeUrl;

    /**
     * 获取歌曲ID 用于获取具体的播放链接
     */
    private int muiscId;

    /**
     * 歌曲名字
     */
    private String name;
    /**
     * 歌手名字
     */
    private String artName;

    /**
     * 音乐封面 url
     */
    private String musicPicUrl;

    /**
     * 点歌的用户
     */
    private User sender;

    // Getter and Setter

    public String getUnikey() {
        return unikey;
    }

    public void setUnikey(String unikey) {
        this.unikey = unikey;
    }

    public String getQRCodeUrl() {
        return QRCodeUrl;
    }

    public void setQRCodeUrl(String QRCodeUrl) {
        this.QRCodeUrl = QRCodeUrl;
    }

    public int getMuiscId() {
        return muiscId;
    }

    public void setMuiscId(int muiscId) {
        this.muiscId = muiscId;
    }

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

    public String getMusicPicUrl() {
        return musicPicUrl;
    }

    public void setMusicPicUrl(String musicPicUrl) {
        this.musicPicUrl = musicPicUrl;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
