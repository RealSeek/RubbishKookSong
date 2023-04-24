package me.realseek.util.apimethod;

import com.google.gson.*;
import me.realseek.Main;
import me.realseek.pojo.Netease;
import me.realseek.pojo.QQMusic;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class QQMusicMethod {
    static QQMusic qqMusic = Main.getQqMusic();
    static OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    public static void setCookie(){
        // 第一步
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String json = "{\"data\":\"" + Main.getInstance().getConfig().get("QQMusic_Url_Cookie") + "\"}";
        RequestBody body = RequestBody.create(mediaType, json);
        try (Response request = client.newCall(
                new Request.Builder()
                        .post(body)
                        .url("http://localhost:3300/user/setCookie")
                        .build()
        ).execute()){
            System.out.println("已设置Cookie");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 第二步
        String QQId = "{\"id\":\"" + Main.getInstance().getConfig().get("QQMusic_ID") + "\"}";
        RequestBody body2 = RequestBody.create(mediaType, QQId);
        try (Response request = client.newCall(
                new Request.Builder()
                        .post(body2)
                        .url("http://localhost:3300/user/getCookie")
                        .build()
        ).execute()){
            System.out.println("已设置QQ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 第三步
        try (Response request = client.newCall(
                new Request.Builder()
                        .post(RequestBody.create("", null))
                        .url("http://localhost:3300/user/cookie")
                        .build()
        ).execute()){
            System.out.println("设置完毕");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 搜索
     */
    public static void getQQMusicInfo(String singName){
        try (Response request = client.newCall(
                new Request.Builder()
                        .get()
                        .url("http://localhost:3300/search?key=" + singName + "&pageNo=1&pageSize=1&t=0")
                        .build()
        ).execute()) {
            // 解析JSON字符串
            JsonObject jsonObj = JsonParser.parseString(request.body().string()).getAsJsonObject();
            // 获取"data"字段对应的Json对象
            JsonObject dataObj = jsonObj.get("data").getAsJsonObject();
            // 获取"list"字段对应的Json数组的第一个元素
            JsonObject firstObj = dataObj.get("list").getAsJsonArray().get(0).getAsJsonObject();
            // 获取歌曲名
            qqMusic.setName(firstObj.get("songname").getAsString());

            // 获取歌曲 mid
            qqMusic.setSongmid(firstObj.get("songmid").getAsString());

            // 获取封面 url
            qqMusic.setPicUrl("https://y.gtimg.cn/music/photo_new/T002R300x300M000"+ firstObj.get("albummid").getAsString() +".jpg");

            // 获取"singer"字段对应的Json数组的第一个元素
            JsonObject singerObj = firstObj.get("singer").getAsJsonArray().get(0).getAsJsonObject();
            // 获取歌手名
            qqMusic.setArtName(singerObj.get("name").getAsString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 歌曲下载链接
     */
    public static String getDownloadUrl(String mid){
        try (Response request = client.newCall(
                new Request.Builder()
                        .get()
                        .url("http://localhost:3300/song/url?id=" + mid)
                        .build()
        ).execute()) {
            JsonObject jsonObject = JsonParser.parseString(request.body().string()).getAsJsonObject();
            String url = jsonObject.get("data").getAsString();
            return url;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
