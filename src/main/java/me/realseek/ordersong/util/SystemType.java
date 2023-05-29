package me.realseek.ordersong.util;

import java.util.Locale;

public class SystemType {
    /**
     * 输出当前的操作系统
     * @return
     */
    public static String getOperatingSystemType() {
        // 获取操作系统
        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);

        if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
            return "Mac OS X";
        } else if (OS.indexOf("win") >= 0) {
            return "Windows";
        } else if (OS.indexOf("nux") >= 0) {
            return "Linux";
        } else {
            return "Unknown";
        }
    }

    /**
     * 处理不同系统的路径
     * @param path
     * @return
     */
    public static String processFilePath(String path) {
        String OS = getOperatingSystemType();
        if (OS.equals("Windows")) {
            return path.replace("/", "\\");
        } else {
            return path.replace("\\", "/");
        }
    }
}
