package me.realseek.util;

import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;

import java.lang.reflect.Field;

public class JNAMethod {
    /**
     * 获取进程 PID
     * @param p
     * @return
     */
    public static long getProcessID(Process p) {
        long result = -1;
        try
        {
            //for windows
            if (p.getClass().getName().equals("java.lang.Win32Process") ||
                    p.getClass().getName().equals("java.lang.ProcessImpl"))
            {
                Field f = p.getClass().getDeclaredField("handle");
                f.setAccessible(true);
                long handl = f.getLong(p);
                Kernel32 kernel = Kernel32.INSTANCE;
                WinNT.HANDLE hand = new WinNT.HANDLE();
                hand.setPointer(Pointer.createConstant(handl));
                result = kernel.GetProcessId(hand);
                f.setAccessible(false);
            }
            //for unix based operating systems
            else if (p.getClass().getName().equals("java.lang.UNIXProcess"))
            {
                Field f = p.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                result = f.getLong(p);
                f.setAccessible(false);
            }
        }
        catch(Exception ex)
        {
            result = -1;
        }
        return result;
    }

    /**
     * 处理不同系统的路径
     * @param path
     * @return
     */
    public static String normalizeAbsolutePath(String path) {
        if (Platform.isWindows()) {
            // Windows 文件分隔符为 \，需要将路径中的 / 替换成 \
            return path;
        } else {
            // Unix-like 系统中文件分隔符为 /，需要将路径中的 / 替换成 \
            return path.replace('\\', '/');
        }
    }
}
