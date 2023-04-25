<img src="https://capsule-render.vercel.app/api?type=transparent&fontColor=703ee5&text=RubbishKookSong&height=150&fontSize=60&desc=%E5%86%99%E7%9A%84%E5%BE%88%E5%B7%AE%E7%9A%84Kook%E7%82%B9%E6%AD%8C%E6%8F%92%E4%BB%B6&descAlignY=75&descAlign=60&animation=fadeIn" />

<h1 align="center">
  RubbishKookSong
</h1>

<p align='center'>
    <a  href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
		<img src="https://img.shields.io/badge/JDK-8+-brightgreen.svg"  alt=""/>
	</a>
    <a  href="https://github.com/SNWCreations/JKook">
		<img src="https://img.shields.io/badge/JKook%20API-3e89cde-brightgreen"  alt=""/>
	</a>
    <a href="https://github.com/SNWCreations/KookBC">
        <img src="https://img.shields.io/badge/KookBC-0.27.0-brightgreen" alt=""/>
    </a>
    <a href="https://opensource.org/licenses/MIT">
        <img src="https://img.shields.io/badge/license-MIT-brightgreen.svg" alt=""/>
    </a>
    <a href="https://nodejs.org/">
        <img src="https://img.shields.io/badge/NodeJS-12%2B-blue" alt=""/>
    </a>
</p>

## 使用说明

此插件并没有任何破解和绕开音乐平台限制的功能，只是通过访问第三方API获取歌曲数据，付费歌曲需要账号本身开通VIP或购买专辑

## 你需要做的

1. 先下载 [KookBC](https://github.com/SNWCreations/KookBC) 并且运行起来一个自己的 Bot,找到 kbc.yml 的 internal-commands.help 更改为 false

2. 部署 [NeteaseCloudMusicApi](https://github.com/Binaryify/NeteaseCloudMusicApi) 和 [QQMusicApi](https://github.com/jsososo/QQMusicApi)

> 这是 NeteaseCloudMusicApi 简单部署流程，详细可以去原仓库查看
```bash
# clone 
git clone https://github.com/Binaryify/NeteaseCloudMusicApi
# 移动到 NeteaseCloudMusicApi
cd NeteaseCloudMusicApi
# 安装依赖
npm install
# 启动服务
node app.js
```

> 这是 QQMusicApi 简单部署流程，详细可以去原仓库查看
```bash
# clone 
git clone https://github.com/jsososo/QQMusicApi
# 移动到 QQMusicApi
cd QQMusicApi
# 安装依赖
npm install
# 启动服务
npm start
```

> **注意：** [QQMusicApi](https://github.com/jsososo/QQMusicApi) 原作者由于忙碌很久没有更新，API需要自己修复一些问题  

> 如果嫌麻烦的话，可以使用本人fork的修复版本 [QQMusicApi](https://github.com/RealSeek/QQMusicApi)

3. 将本插件放入 KookBc 的 plugins 内并启动 Bot

4. 当控制台输出插件启动时，说明插件已经启用

## 配置文件说明

当你启动一次 Bot 后 你可以先关闭你的 Bot , 进入 plugins.RubbishKookSong.config.yml 编辑插件的配置文件

```bash
# 网易云api扫码登录返回的cookie
Netease_Url_Cookie: ""

# QQ音乐api需要登录的QQ号
QQMusic_ID: ""

# QQ音乐api登录cookie 注意必须和QQ号对应
QQMusic_Url_Cookie: ""

# 插件管理员ID
BotAdmin: ["2038278961", "管理员ID"]
```

`Netease_Url_Cookie` 这一项不需要你填入，启动 Bot 输入网易云登录后扫描二维码会自动填入

`QQMusic_ID` 这一项需要你填入要登录的QQ号，要和 API 设置的 QQ 号一样才有效

`QQMusic_Url_Cookie` 这一项需要你登录网页版 QQ 音乐官网获取 cookie，保存后输入指令刷新才算登录完成

`BotAdmin` 管理员可以使用指令停止 Bot, 管理员 ID 可以在 Kook 内打开开发者模式，然后右键用户即会出现一个复制 ID 的选项

> **注意：网页版 QQ 音乐官网获取 cookie 只有一天有效期，暂无解决办法，推荐使用 [qqmusic-cookie-porter](https://github.com/jsososo/qqmusic-cookie-porter) 来半自动获取 cookie**


## QQMusicApi 修改 QQ 号

项目默认qq号 1234567，可以通过修改 bin/config.js 或设置启动参数 QQ=7654321 npm start

如果使用的是微信号，则将 bin/config.js 中的 qq好改为 wxuin，具体数值可以在网页版qq音乐的 cookie 中获取

## 指令说明

启动 Bot 后 在频道内输入 /help 即可返回详细的指令菜单

> 菜单由我的朋友 花祈kora 几分钟简单绘制，后续会优化

## 社区

如果你需要帮助或者试用 Bot, 可以加入官方社区：[点我加入](https://kook.top/JOHwp4)
