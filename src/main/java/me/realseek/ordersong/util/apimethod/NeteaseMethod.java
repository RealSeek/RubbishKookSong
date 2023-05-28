package me.realseek.ordersong.util.apimethod;

import cn.hutool.core.net.url.UrlBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.realseek.ordersong.Main;
import me.realseek.ordersong.api.NeteaseAPI;
import me.realseek.ordersong.pojo.Netease;
import okhttp3.*;
import snw.jkook.entity.User;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static me.realseek.ordersong.util.Config.getNeteaseAPIURL;

public class NeteaseMethod {
    static String qrKey;
    static Netease netease = Main.getNetease();

    static OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    /**
     * 用于生成二维码
     */
    public static String getUnikey() {
        // 调用二维码key的生成接口
        try (Response request = client.newCall(
                new Request.Builder()
                        .post(RequestBody.create("", null))
                        //.url("http://localhost:3000/login/qr/key")
                        .url(UrlBuilder.of(getNeteaseAPIURL())
                                .addPath("login")
                                .addPath("qr")
                                .addPath("key")
                                .toString())
                        .build()
        ).execute()) {
            // 获取key
            JsonObject jsonObject = JsonParser.parseString(request.body().string()).getAsJsonObject();
            qrKey = jsonObject.getAsJsonObject("data").get("unikey").getAsString();
            return qrKey;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用于获取二维码的url
     *
     * @param qrKey
     * @return
     */
    public static String getQRCode(String qrKey) {
        // 调用二维码生成接口
        try (Response request = client.newCall(
                new Request.Builder()
                        .post(RequestBody.create("", null))
                        //.url("http://localhost:3000/login/qr/create?key=" + qrKey)
                        .url(UrlBuilder.of(getNeteaseAPIURL())
                                .addPath("login")
                                .addPath("qr")
                                .addPath("create")
                                .addQuery("key",qrKey)
                                .toString())
                        .build()
        ).execute()) {
            // 获取二维码
            JsonObject jsonObject = JsonParser.parseString(request.body().string()).getAsJsonObject();
            String qrurl;
            qrurl = jsonObject.getAsJsonObject("data").get("qrurl").getAsString();
            return qrurl;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查登录状态
     */
    public static int CheckQRStatus() {
        try (Response request = client.newCall(
                new Request.Builder()
                        .post(RequestBody.create("", null))
                        //.url("http://localhost:3000/login/qr/check?key=" + netease.getUnikey() + TimeStamp.spawnTimeStamp())
                        .url(UrlBuilder.of(getNeteaseAPIURL())
                                .addPath("login")
                                .addPath("qr")
                                .addPath("check")
                                .addQuery("key",netease.getUnikey())
                                .addQuery("timestamp",System.currentTimeMillis())
                                .toString())
                        .build()
        ).execute()) {
            // 获取扫码状态
            JsonObject jsonObject = JsonParser.parseString(request.body().string()).getAsJsonObject();
            if (jsonObject.get("code").getAsInt() != 803) {
                System.out.println("登录状态:" + jsonObject.get("code").getAsInt());
                return 404;
            } else {
                System.out.println("登录完成！");
                if (jsonObject.get("code").getAsInt() == 800) {
                    // 二维码过期了
                    System.out.println("二维码过期，请重新生成");
                    return 800;
                } else if (jsonObject.get("code").getAsInt() == 802) {
                    System.out.println("等待确认中。。。");
                    return 802;
                } else {
                    System.out.println("登录完成");
                    // 拿到 cookie 后保存
                    Main.getInstance().getConfig().set("Netease_Url_Cookie", jsonObject.get("cookie").getAsString());
                    Main.getInstance().saveConfig();
                    // 重载配置文件
                    Main.getInstance().reloadConfig();
                    System.out.println("cookie为：\n" + jsonObject.get("cookie").getAsString());
                    return 803;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取歌曲ID
     * @param musicName
     * @return
     */
    public static int getMusicId(String musicName) {
        try (Response response = client.newCall(
                new Request.Builder()
                        .post(RequestBody.create("", null))
                        //.url("http://localhost:3000/cloudsearch?keywords=" + musicName + "&limit=1")
                        .url(UrlBuilder.of(getNeteaseAPIURL())
                                .addPath("cloudsearch")
                                .addQuery("keywords",musicName)
                                .addQuery("limit",1)
                                .toString())
                        .build()
        ).execute()) {
            JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
            for (int i = 0; i < jsonObject.getAsJsonObject("result").getAsJsonArray("songs").size(); i++) {
                JsonObject songObject = jsonObject.getAsJsonObject("result").getAsJsonArray("songs").get(i).getAsJsonObject();
                // 设置歌曲id
                netease.setMuiscId(songObject.get("id").getAsInt());
            }
            // 添加ID到List
            // Main.getMusicIdList().add(netease.getMuiscId());
        } catch (IOException e) {
            Main.getMessage().sendToSource("未获取到相关歌曲，请确认是否输入正确");
        }
        return netease.getMuiscId();
    }

    /**
     * 获取歌曲详细信息
     * @param musicId
     */
    public static void getMusicInfo(int musicId){
        try (Response response = client.newCall(
                new Request.Builder()
                        .post(RequestBody.create("", null))
                        //.url("http://localhost:3000/song/detail?ids=" + musicId + TimeStamp.spawnTimeStamp())
                        .url(UrlBuilder.of(getNeteaseAPIURL())
                                .addPath("song")
                                .addPath("detail")
                                .addQuery("ids",musicId)
                                .addQuery("timestamp",System.currentTimeMillis())
                                .toString())
                        .build()
        ).execute()) {
            // 处理音乐返回的详细信息
            JsonObject musicObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
            JsonArray songsArray = musicObject.getAsJsonArray("songs");
            JsonObject songObj = songsArray.get(0).getAsJsonObject();

            // 开始赋值
            // 歌曲名字
            netease.setName(songObj.get("name").getAsString());
            // 歌手名字 只取一个
            netease.setArtName(songObj.getAsJsonArray("ar")
                    .get(0).getAsJsonObject()
                    .get("name").getAsString());
            // 歌曲封面 url
            netease.setMusicPicUrl(songObj.getAsJsonObject("al")
                    .get("picUrl").getAsString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取歌曲下载 url
     * @param musicId
     * @return
     */
    public static String getMusicDownload(int musicId){
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String json = "{\"cookie\":\"" + Main.getInstance().getConfig().get("Netease_Url_Cookie") + "\"}";
        RequestBody body = RequestBody.create(mediaType, json);
        try (Response response = client.newCall(
                new Request.Builder()
                        .post(body)
                        //.url("http://localhost:3000/song/download/url?id=" + musicId + "&br=320000" + TimeStamp.spawnTimeStamp())
                        .url(UrlBuilder.of(getNeteaseAPIURL())
                                .addPath("song")
                                .addPath("download")
                                .addPath("url")
                                .addQuery("id",musicId)
                                .addQuery("br",320000)
                                .addQuery("timestamp",System.currentTimeMillis())
                                .toString())
                        .build()
        ).execute()) {
            // debug 用日志 这 url 返回歌曲下载 url
            String msg;
            JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
            if (jsonObject.getAsJsonObject("data").get("url").isJsonNull()) {
                // System.out.println("V1获取失败，进入V2获取");
                msg = getMusicDownloadV2(musicId);
                return msg;
            } else {
                // System.out.println("V1获取成功");
                String url = jsonObject.getAsJsonObject("data").get("url").getAsString();
                return url;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 第二种获取歌曲URL的方法
     * @param musicId
     * @return
     */
    public static String getMusicDownloadV2(int musicId){
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String json = "{\"cookie\":\"" + Main.getInstance().getConfig().get("Netease_Url_Cookie") + "\"}";
        RequestBody body = RequestBody.create(mediaType, json);
        try (Response response = client.newCall(
                new Request.Builder()
                        .post(body)
                        //.url("http://localhost:3000/song/url/v1?id=" + musicId + "&level=exhigh")
                        .url(UrlBuilder.of(getNeteaseAPIURL())
                                .addPath("song")
                                .addPath("url")
                                .addPath("v1")
                                .addQuery("id",musicId)
                                .addQuery("level","exhigh")
                                .toString())
                        .build()
        ).execute()) {
            // debug 用日志 这 url 返回歌曲下载 url
            String msg;
            JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
            if (jsonObject.getAsJsonArray("data").get(0).getAsJsonObject().get("url").isJsonNull()) {
                System.out.println("cookie过期或无版权");
                msg = "cookie过期或无版权";
                return msg;
            } else {
                String url = jsonObject.getAsJsonArray("data").get(0).getAsJsonObject().get("url").getAsString();
                return url;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 网易云歌单
     * @param musicListId
     */
    public static void getMusicListInfo(String musicListId, User sender){
        boolean find = true;
        try (Response response = client.newCall(
                new Request.Builder()
                        .post(RequestBody.create("", null))
                        //.url("http://localhost:3000/playlist/track/all?id=" + musicListId)
                        .url(UrlBuilder.of(getNeteaseAPIURL())
                                .addPath("playlist")
                                .addPath("track")
                                .addPath("all")
                                .addQuery("id",musicListId)
                                .toString())
                        .build()
        ).execute()) {
            // 处理音乐返回的详细信息
            JsonObject musicObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
            // 获取"songs"数组
            JsonArray songsArray = musicObject.getAsJsonArray("songs");
            // 遍历"songs"数组中的每个对象
            for (JsonElement songElement : songsArray){
                // 获取歌曲 id
                netease.setMuiscId(songElement.getAsJsonObject().get("id").getAsInt());
                // System.out.println(netease.getMuiscId());

                // 获取歌曲名字
                netease.setName(songElement.getAsJsonObject().get("name").getAsString());
                // System.out.println(netease.getName());

                // 获取歌曲歌手 获取ar节点
                // 获取ar节点
                JsonArray ar = songElement.getAsJsonObject().getAsJsonArray("ar");
                // 遍历ar节点
                for (JsonElement arElement : ar) {
                    // 获取name节点的值
                    netease.setArtName(arElement.getAsJsonObject().get("name").getAsString());
                }

                // 获取歌曲封面
                netease.setMusicPicUrl(songElement.getAsJsonObject().getAsJsonObject("al").get("picUrl").getAsString());
                // System.out.println(songElement.getAsJsonObject().getAsJsonObject("al").get("picUrl").getAsString() + "\n");
                String musicDownloadUrl = NeteaseAPI.neteaseMusicDownloadUrl(netease.getMuiscId());
                if (find){
                    // 搜索状态
                    Thread.sleep(250L);
                }
                if (musicDownloadUrl.equals("cookie过期或无版权")){
                    find = false;
                    System.out.println(netease.getName() + " - " + netease.getArtName() + "没有版权，已跳过");
                }else {
                    // 将对象存进集合
                    // new 一个对象 并把数据传入
                    Netease neteaseList = new Netease(netease, sender);
                    Main.getMusicList().add(neteaseList);
                }
            }
            // 结束List添加后
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
