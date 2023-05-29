package me.realseek.ordersong.voice;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.realseek.ordersong.ffmpeg.FFmpeg;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.*;

public class SimpleWebSocketListener extends WebSocketListener {
    static String rtp;
    private final CompletableFuture<String> future;
    private int stage = 1;
    private volatile boolean dead = false;
    private final Callable<Void> onDead;
    static int FFmpegPort;
    public SimpleWebSocketListener(CompletableFuture<String> future, Callable<Void> onDead) {
        this.future = future;
        this.onDead = onDead;
    }

    public void onOpen(WebSocket webSocket, Response response) {
        // 连接成功，发送 ping 消息
        webSocket.send("ping");
        // 开始定时发送 ping 消息
        startPing(webSocket);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        if ("pong".equals(text)) {
            // 接收到 pong 消息，继续发送 ping 消息
            startPing(webSocket);
        }
        // super.onMessage(webSocket, text);
        if (dead) return;
        JsonObject res = JsonParser.parseString(text).getAsJsonObject();
        if (res.has("notification")) {
            if (res.has("method")) {
                if (Objects.equals(res.get("method").getAsString(), "disconnect")) { // connection is dead
                    dead = true;
                    if (onDead != null) {
                        try {
                            onDead.call();
                        } catch (Exception ignored) {
                        }
                    }
                    webSocket.close(1000, "User Closed Service");
                }
            }
            return;
        }
        if (stage++ == 3) {
            JsonObject data = res.getAsJsonObject("data");
            String addr = data.get("ip").getAsString();
            int port = data.get("port").getAsInt();
            int rtcpPort = data.get("rtcpPort").getAsInt();
            String id = data.get("id").getAsString();
            FFmpegPort = rtcpPort;
            rtp = String.format("rtp://%s:%s?rtcpport=%s", addr, port, rtcpPort);
            future.complete(rtp);
            // System.out.println("获取到的rtp:" + rtp);
            JsonObject object = JsonParser.parseString(Constants.STAGE_4).getAsJsonObject();
            JsonObject data1 = object.getAsJsonObject("data");
            data1.remove("transportId");
            data1.addProperty("transportId", id);
            webSocket.send(new Gson().toJson(object));
            // ZMQ 启动
            // 新开一个线程处理 避免进程堵塞
            FFmpeg FFmpeg = new FFmpeg();
            Thread ZMQThread = new Thread(FFmpeg);
            ZMQThread.start();
        }
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        // 连接关闭，停止发送 ping 消息
        stopPing();
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        // 连接失败，停止发送 ping 消息
        stopPing();
    }

    public static String getRtp() {
        return rtp;
    }

    private static ScheduledFuture<?> pingSchedule;

    private void startPing(WebSocket webSocket) {
        // 定时发送 ping 消息
        pingSchedule = Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {
                    webSocket.send("ping");
                }, 10, 10, TimeUnit.SECONDS);
    }

    private static void stopPing() {
        // 停止发送 ping 消息
        if (pingSchedule != null) {
            pingSchedule.cancel(false);
            pingSchedule = null;
        }
    }

    public static void stop(){
        stopPing();
    }

    public static int getFFmpegPort() {
        return FFmpegPort;
    }

}
