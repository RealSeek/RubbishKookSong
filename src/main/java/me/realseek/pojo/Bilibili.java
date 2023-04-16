package me.realseek.pojo;

public class Bilibili {
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
}
