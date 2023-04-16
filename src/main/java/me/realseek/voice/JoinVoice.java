package me.realseek.voice;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.realseek.ffmpeg.FFmpeg;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class JoinVoice {
    static private WebSocket webSocket;
    String gatewayWs;
    static OkHttpClient httpClient = new OkHttpClient();
    public Future<String> joinVoice(String ChannelId, String BotToken, Callable<Void>onDead) {
        disconnect();
        webSocket = null;
        try (Response response = httpClient.newCall(
                new Request.Builder()
                        .get()
                        .url("https://www.kaiheila.cn/api/v3/gateway/voice?channel_id=" + ChannelId)
                        .addHeader("Authorization", String.format("Bot %s", BotToken))
                        .build()
        ).execute()){
            if (response.code() != 200){
                throw new IllegalStateException();
            }
            assert response.body() != null;
            JsonObject element = JsonParser.parseString(response.body().string()).getAsJsonObject();
            if (element.get("code").getAsInt() != 0) {
                throw new IllegalStateException();
            }
            gatewayWs = element.getAsJsonObject("data").get("gateway_url").getAsString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CompletableFuture<String> future = new CompletableFuture<>();

        // endregion
        webSocket = httpClient.newWebSocket(
                new Request.Builder()
                        .url(gatewayWs)
                        .build(),
                new SimpleWebSocketListener(future, onDead)
        );

        webSocket.send(randomId(Constants.STAGE_1));
        webSocket.send(randomId(Constants.STAGE_2));
        webSocket.send(randomId(Constants.STAGE_3));
        return future;
    }

    public static void disconnect() {
        if (webSocket != null) {
            webSocket.close(1000, "User Closed Service");
            FFmpeg.getZMQ().destroy();
        }
    }

    private static String randomId(String constant) {
        JsonObject object = JsonParser.parseString(constant).getAsJsonObject();
        object.remove("id");
        object.addProperty("id", new SecureRandom().nextInt(8999999) + 1000000);
        return new Gson().toJson(object);
    }

    public static OkHttpClient getHttpClient() {
        return httpClient;
    }

    public static WebSocket getWebSocket() {
        return webSocket;
    }
}

