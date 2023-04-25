package me.realseek.ffmpeg;

import me.realseek.Main;
import me.realseek.voice.SimpleWebSocketListener;

import java.io.IOException;

public class FFmpeg implements Runnable{

    static String FFmpegPath = Main.getFFmpegPath();

    /**
     * ZMQ 进程
     */
    static Process ZMQ;

    // 线程调用的方法
    @Override
    public void run() {
        startZMQ();
    }

    public static void startZMQ(){
        // zmq
        String command1 = String.format
                ("%s -re -loglevel level+info -nostats -stream_loop -1 -i zmq:tcp://127.0.0.1:" + SimpleWebSocketListener.getFFmpegPort() + " -map 0:a:0 -acodec libopus -ab 128k -filter:a volume=0.15 -ac 2 -ar 48000 -f tee [select=a:f=rtp:ssrc=1357:payload_type=100]%s"
                        , FFmpegPath,SimpleWebSocketListener.getRtp());
        ProcessBuilder pb1 = new ProcessBuilder(command1.split(" "));
        pb1.redirectErrorStream(true);
        try {
            ZMQ = pb1.start();
            System.out.println("ZMQ服务已启动！");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Process getZMQ() {
        return ZMQ;
    }

}
