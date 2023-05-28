package me.realseek.ordersong.voice;

public class Constants {
    public static final String STAGE_1 = "{\"request\":true,\"id\":1000000,\"method\":\"getRouterRtpCapabilities\",\"data\":{}}";
    public static final String STAGE_2 = "{\"data\":{\"displayName\":\"\"},\"id\":1000000,\"method\":\"join\",\"request\":true}";
    public static final String STAGE_3 = "{\"data\":{\"comedia\":true,\"rtcpMux\":false,\"type\":\"plain\"},\"id\": 1000000,\"method\":\"createPlainTransport\",\"request\":true}";
    public static final String STAGE_4 = "{\"data\":{\"appData\":{},\"kind\":\"audio\",\"peerId\":\"\",\"rtpParameters\":{\"codecs\":[{\"channels\":2,\"clockRate\":48000,\"mimeType\":\"audio/opus\",\"parameters\":{\"sprop-stereo\":1},\"payloadType\":100}],\"encodings\":[{\"ssrc\":1357}]},\"transportId\":\"\"},\"id\":1000000,\"method\":\"produce\",\"request\":true}";
}
