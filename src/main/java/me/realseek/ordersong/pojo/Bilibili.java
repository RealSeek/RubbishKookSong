package me.realseek.ordersong.pojo;

import snw.jkook.entity.User;

public class Bilibili {
    public Bilibili() {
    }

    public Bilibili(Bilibili bilibili, User sender) {
        this.imgKey = bilibili.getImgKey();
        this.sub = bilibili.getSub();
        this.qrUrl = bilibili.getQrUrl();
        this.qrcode_key = bilibili.getQrcode_key();
        this.bvNum = bilibili.getBvNum();
        this.pNum = bilibili.getpNum();
        this.pic = bilibili.getPic();
        this.title = bilibili.getTitle();
        this.cid = bilibili.getCid();
        this.audioUrl = bilibili.getAudioUrl();
        this.sender = sender;
        this.upName = bilibili.getUpName();
    }

    private String imgKey;
    private String sub;

    /**
     * 二维码 url
     */
    private String qrUrl;
    /**
     * 识别二维码的 key
     */
    private String qrcode_key;
    /**
     * 视频BV号
     */
    private String bvNum;
    /**
     * 分P数据
     */
    private int pNum = 0;
    /**
     * 视频封面url
     */
    private String pic;
    /**
     * 视频标题
     */
    private String title;
    /**
     * 视频 cid
     */
    private int cid;
    /**
     * 视频音频流
     */
    private String audioUrl;
    /**
     * 点歌的用户
     */
    private User sender;

    /**
     * UP主
     */
    private String upName;

    // Getter and Setter
    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getBvNum() {
        return bvNum;
    }

    public void setBvNum(String bvNum) {
        this.bvNum = bvNum;
    }

    public int getpNum() {
        return pNum;
    }

    public void setpNum(int pNum) {
        this.pNum = pNum;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getUpName() {
        return upName;
    }

    public void setUpName(String upName) {
        this.upName = upName;
    }

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }

    public String getQrcode_key() {
        return qrcode_key;
    }

    public void setQrcode_key(String qrcode_key) {
        this.qrcode_key = qrcode_key;
    }

    public String getImgKey() {
        return imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }
}
