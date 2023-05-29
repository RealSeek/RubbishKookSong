package me.realseek.ordersong.util.apimethod;

import cn.hutool.core.net.url.UrlBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.realseek.ordersong.Main;
import me.realseek.ordersong.pojo.Bilibili;
import me.realseek.ordersong.util.Wbi;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.realseek.ordersong.util.Config.getBilibiliCookie;

public class BilibiliMethod {
    static Bilibili bilibili = Main.getBilibili();

    final static String bilibiliAPIURL = "https://api.bilibili.com";

    static OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    /**
     * 获取扫码登录 url
     * @return
     */
    public static String getBiliLoginUrl() {
        try (Response response = client.newCall(
                new Request.Builder()
                        .get()
                        .url("https://passport.bilibili.com/x/passport-login/web/qrcode/generate")
                        .build()
        ).execute()) {
            JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
            // 获取 登录url
            bilibili.setQrUrl(jsonObject.getAsJsonObject("data").get("url").getAsString());
            // 获取 key
            bilibili.setQrcode_key(jsonObject.getAsJsonObject("data").get("qrcode_key").getAsString());
            System.out.println("登录url: " + bilibili.getQrUrl());
            System.out.println("key: " + bilibili.getQrcode_key());
            return bilibili.getQrUrl();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查登录状态
     */
    public static int CheckQRStatus() {
        try (Response response = client.newCall(
                new Request.Builder()
                        .get()
                        .url(UrlBuilder.of("https://passport.bilibili.com/x/passport-login/web/qrcode/poll")
                                .addQuery("qrcode_key", bilibili.getQrcode_key())
                                .toString())
                        .build()
        ).execute()) {
            // 验证是否正确
            String fullCookie = null;
            // 获取扫码状态
            JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
            if (jsonObject.getAsJsonObject("data").get("code").getAsInt() == 0) {
                System.out.println("登录完成");
                // 拿到 cookie 后保存
                if (response.headers("Set-Cookie").size() > 0) {
                    List<String> cookies = response.headers("Set-Cookie");
                    // 在这里处理 Cookie
                    StringBuilder cookieBuilder = new StringBuilder();
                    for (String cookie : cookies) {
                        cookieBuilder.append(cookie).append("; ");
                    }
                    fullCookie = cookieBuilder.toString();
                }
                Matcher cookie = Pattern.compile("SESSDATA=[^;]+").matcher(fullCookie);
                cookie.find();
                Main.getInstance().getConfig().set("Bilibili_Cookie", cookie.group());
                Main.getInstance().saveConfig();
                // 重载配置文件
                Main.getInstance().reloadConfig();
                System.out.println("cookie为：\n" + cookie.group());
                return 0;
            } else {
                if (jsonObject.getAsJsonObject("data").get("code").getAsInt() == 86038) {
                    // 二维码过期了
                    System.out.println("二维码过期，请重新生成");
                    return 86038;
                } else if (jsonObject.getAsJsonObject("data").get("code").getAsInt() == 86090) {
                    // 二维码已扫码未确认
                    System.out.println("等待确认中。。。");
                    return 86090;
                } else {
                    // 未扫码
                    System.out.println("等待扫码中...");
                    return 86101;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


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
        try (Response response = client.newCall(
                new Request.Builder()
                        .get()
                        .addHeader("Cookie", getBilibiliCookie())
                        //.url("https://api.bilibili.com/x/web-interface/view?bvid=" + BVId)
                        .url(UrlBuilder.of(bilibiliAPIURL)
                                .addPath("x")
                                .addPath("web-interface")
                                .addPath("view")
                                .addQuery("bvid",BVId)
                                .toString())
                        .build()
        ).execute()) {
            JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
            // 视频图片url
            bilibili.setPic(jsonObject.getAsJsonObject("data").get("pic").getAsString());
            // 视频标题
            bilibili.setTitle(jsonObject.getAsJsonObject("data").get("title").getAsString());
            // 分P
            bilibili.setCid(jsonObject.getAsJsonObject("data").getAsJsonArray("pages").
                    get(bilibili.getpNum()).getAsJsonObject().get("cid").getAsInt());
            // UP名字
            bilibili.setUpName(jsonObject.get("data").getAsJsonObject().get("owner").getAsJsonObject().get("name").getAsString());
            // System.out.println("获取到B站视频封面，标题和cid了");
            // System.out.println("得到数据如下：\n"
            //         + bilibili.getPic() + "\n"
            //         + bilibili.getTitle() + "\n"
            //         + bilibili.getCid() + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取音频流
     * @param BVId
     * @return
     */
    public static String getBiliVideoUrl(String BVId, int cid){
        // timestamp
        long unixTimestamp = System.currentTimeMillis() / 1000L;

        JsonObject temp = null;
        try (Response response = client.newCall(
                new Request.Builder()
                        .get()
                        .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                        .addHeader("Cookie", getBilibiliCookie())
                        //.url("https://api.bilibili.com/x/player/playurl?fnval=16&bvid=" + BVId + "&cid=" + bilibili.getCid())
                        .url(UrlBuilder.of(bilibiliAPIURL)
                                .addPath("x")
                                .addPath("player")
                                .addPath("wbi")
                                .addPath("playurl")
                                .addQuery("fnval", 16)
                                .addQuery("bvid", BVId)
                                .addQuery("cid", cid)
                                .addQuery("wts", unixTimestamp)
                                .addQuery("w_rid", Wbi.getW_rid(unixTimestamp, BVId, cid))
                                .toString())
                        .build()
        ).execute()) {
            JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
            temp = jsonObject;
            String audioUrl = jsonObject.get("data").getAsJsonObject()
                    .get("dash").getAsJsonObject()
                    .get("audio").getAsJsonArray().get(0).getAsJsonObject()
                    .get("baseUrl").getAsString();
            bilibili.setAudioUrl(audioUrl);
            // System.out.println("拿到音频流为：\n" + bilibili.getAudioUrl());
            // 然后下载
        } catch (IOException | NullPointerException e) {
            System.out.println(temp);
            throw new RuntimeException(e);
        }
        return bilibili.getAudioUrl();
    }
}
