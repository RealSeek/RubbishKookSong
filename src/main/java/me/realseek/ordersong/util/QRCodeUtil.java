package me.realseek.ordersong.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import me.realseek.ordersong.Main;

/**
 * 二维码生成工具类
 */
public class QRCodeUtil {
    public static void createQRCode(String url){
        // 生成指定url对应的二维码到文件，宽和高都是300像素
        QrCodeUtil.generate(url, 300, 300, FileUtil.file(Main.getResPath() + "\\QRCode.jpg"));
    }
}