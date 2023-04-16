package me.realseek.util.apimethod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.realseek.Main;
import me.realseek.pojo.Bilibili;
import me.realseek.pojo.Netease;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BilibiliMethod {
    static Bilibili bilibili = Main.getBilibili();

    static OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    /**
     * 设置BV号和分P号
     * @param url
     */
    public static void setBiliBVNumAndPnum(String url){
        bilibili.setpNum(0);
        // 使用正则提取BV号
        Pattern BVNum = Pattern.compile("BV([A-Za-z0-9]+)");
        Pattern pNum = Pattern.compile("p=(\\d+)");
        Matcher BV = BVNum.matcher(url);
        Matcher p = pNum.matcher(url);
        if (BV.find()) {
            if (p.find()) {
                // 设置BV号
                System.out.println(BV.group());
                bilibili.setBvNum(BV.group());
                // 处理分P
                String num = p.group();
                bilibili.setpNum(Integer.parseInt(num.substring(2)) - 1);
                // System.out.println("获取到BV和分P");
            }else {
                bilibili.setBvNum(BV.group());
                // System.out.println("只获取到BV，p默认:" + bilibili.getpNum() );
            }
        }
    }

    /**
     * 拿B站视频详细信息
     * @param BVId
     */
    public static void getBiliVideoInfo(String BVId){
        try (Response request = client.newCall(
                new Request.Builder()
                        .get()
                        .url("https://api.bilibili.com/x/web-interface/view?bvid=" + BVId)
                        .build()
        ).execute()) {
            JsonObject jsonObject = JsonParser.parseString(request.body().string()).getAsJsonObject();
            // 视频图片url
            bilibili.setPic(jsonObject.getAsJsonObject("data").get("pic").getAsString());
            // 视频标题
            bilibili.setTitle(jsonObject.getAsJsonObject("data").get("title").getAsString());
            // 分P
            bilibili.setCid(jsonObject.getAsJsonObject("data").getAsJsonArray("pages").
                    get(bilibili.getpNum()).getAsJsonObject().get("cid").getAsInt());
            // System.out.println("获取到B站视频封面，标题和cid了");
            // System.out.println("得到数据如下：\n"
            //         + bilibili.getPic() + "\n"
            //         + bilibili.getTitle() + "\n"
            //         + bilibili.getCid() + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getBiliVideoUrl(String BVId){
        try (Response request = client.newCall(
                new Request.Builder()
                        .get()
                        .url("https://api.bilibili.com/x/player/playurl?fnval=16&bvid=" + BVId + "&cid=" + bilibili.getCid())
                        .build()
        ).execute()) {
            JsonObject jsonObject = JsonParser.parseString(request.body().string()).getAsJsonObject();
            String audioUrl = jsonObject.get("data").getAsJsonObject()
                    .get("dash").getAsJsonObject()
                    .get("audio").getAsJsonArray().get(0).getAsJsonObject()
                    .get("baseUrl").getAsString();
            bilibili.setAudioUrl(audioUrl);
            // System.out.println("拿到音频流为：\n" + bilibili.getAudioUrl());
            // 然后下载
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bilibili.getAudioUrl();
    }
}
