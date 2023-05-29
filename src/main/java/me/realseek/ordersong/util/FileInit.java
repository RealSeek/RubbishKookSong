package me.realseek.ordersong.util;

import me.realseek.ordersong.Main;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileInit {
    public static void FileInit(){
        String os = "Windows";

        File res = new File(SystemType.processFilePath(Main.getInstance().getDataFolder().getPath()));
        File configFile = new File(SystemType.processFilePath(Main.getInstance().getDataFolder().getPath() + "\\config.yml"));
        File ffmpegFile = new File(SystemType.processFilePath(Main.getInstance().getDataFolder().getPath() + "\\ffmpeg.exe"));
        File musicFile = new File(SystemType.processFilePath(Main.getInstance().getDataFolder().getPath() + "\\radio.mp3"));
        Main.setResPath(res.getAbsolutePath());
        Main.setMp3Path(musicFile.getAbsolutePath());

        // 若为 linux 则需要自行安装 ffmpeg
        if (os.equals(SystemType.getOperatingSystemType())){
            Main.setFfmpegPath(ffmpegFile.getAbsolutePath());
        }else Main.setFfmpegPath("ffmpeg");


        // 获取token
        File kbc = new File("kbc.yml");
        // 创建一个 Scanner 对象来读取文件内容
        // 获取 token
        try (Scanner scanner = new Scanner(kbc)) {
            // 使用正则表达式匹配 token 的值
            Pattern pattern = Pattern.compile("token:\\s*\"(.*)\"");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    // 输出 token 的值
                    Main.setToken(matcher.group(1));
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 判断 help 是否关闭
        try {
            InputStream input = new FileInputStream(kbc);
            Yaml yaml = new Yaml();
            Map<String, Object> obj = yaml.load(input);

            // 检查 help 命令是否启用（即设置为 true）
            boolean helpEnabled = obj.containsKey("internal-commands")
                    && ((Map<String, Object>) obj.get("internal-commands")).containsKey("help")
                    && (Boolean) ((Map<String, Object>) obj.get("internal-commands")).get("help");

            if (!helpEnabled) {
                Main.getInstance().getLogger().info("当前help为关闭");
                // 如果 help 命令已禁用（即设置为 false），则执行一些操作。
            }else {
                Main.getInstance().getLogger().warn("注意！你没有修改 kbc.yml 的 internal-commands.help 为 false");
                Main.getInstance().getLogger().warn("为了防止注册指令冲突，请你先将其设置为 false 后再使用本插件");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // 保存资源到 config
        if (os.equals(SystemType.getOperatingSystemType())) {
            if (!ffmpegFile.isFile()) {
                System.out.println("未检测到ffmpeg，已为你重新加载");
                Main.getInstance().saveResource("ffmpeg.exe", false, false);
            }
        }else {
            Main.getInstance().getLogger().info("你的系统为Linux，需要自己安装ffmpeg");
        }

        if (!configFile.isFile()){
            System.out.println("未检测到配置文件，已为你重新加载");
            Main.getInstance().saveDefaultConfig();
        }

        // 防止出现推流文件出现问题
        File mp3 = new File(Main.getResPath() + "\\radio.mp3");
        if (mp3.exists()){
            mp3.delete();
            System.out.println("检测到radio.mp3 , 为了确保第一次推流不会出现问题，已删除");
        }
    }
}
