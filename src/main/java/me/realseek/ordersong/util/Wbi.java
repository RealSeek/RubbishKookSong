package me.realseek.ordersong.util;

import cn.hutool.crypto.digest.DigestUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wbi {
    static OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    /**
     * 获取当天的 key
     * @return
     */
    public static String getNewMixinKey() {
        Pattern p = Pattern.compile("https://i0.hdslb.com/bfs/wbi/(.+?)\\.png");
        try (Response response = client.newCall(
                new Request.Builder()
                        .get()
                        // .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                        .url("https://api.bilibili.com/x/web-interface/nav")
                        .build()
        ).execute()) {
            JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();

            Matcher m1 = p.matcher(jsonObject.getAsJsonObject("data")
                    .getAsJsonObject("wbi_img")
                    .get("img_url").getAsString());
            m1.find();
            String imgKey = m1.group(1);

            Matcher m2 = p.matcher(jsonObject.getAsJsonObject("data")
                    .getAsJsonObject("wbi_img")
                    .get("sub_url").getAsString());
            m2.find();
            String subKey = m2.group(1);
            return imgKey + subKey;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算出需要的 w_rid
     * @param unixTimestamp
     * @param BVid
     * @return
     */
    public static String getW_rid(long unixTimestamp ,String BVid, int cid) {
        // 打乱重排
        String reMap = getMixinKey(getNewMixinKey());
        // 拼接参数
        String parameters = "bvid=" + BVid + "&cid=" + cid + "&fnval=16&wts=" + unixTimestamp + reMap;

        return DigestUtil.md5Hex(parameters);
    }

    private static final int[] mixinKeyEncTab = {
            46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45, 35, 27, 43, 5, 49,
            33, 9, 42, 19, 29, 28, 14, 39, 12, 38, 41, 13, 37, 48, 7, 16, 24, 55, 40,
            61, 26, 17, 0, 1, 60, 51, 30, 4, 22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11,
            36, 20, 34, 44, 52
    };

    /**
     * 打乱
     * @param orig
     * @return
     */
    public static String getMixinKey(String orig) {
        StringBuilder sb = new StringBuilder();
        for (int index : mixinKeyEncTab) {
            sb.append(orig.charAt(index));
        }
        return sb.toString().substring(0, 32);
    }
}
