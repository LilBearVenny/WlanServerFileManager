# WlanServerFileManager

## 📖 项目简介 (Introduction)
ServerBridge是一款专为局域网（LAN）环境设计的跨平台文件传输工具。它能够让连接在同一路由器下的 Android 设备（手机、平板）与 Windows/Linux PC 之间轻松实现文字、图片及文件的互传。

## ✨ 功能特点 (Features)
跨设备传输：支持 Android 客户端与 Windows/Linux PC 服务端之间的数据往来。
多格式支持：支持纯文本、图片以及各类文件的上传与下载。
低延迟体验：基于局域网连接，无需经过公网服务器，速度快且隐私性高。

该项目采用 C/S (Client/Server) 架构：
服务端 (Server)：部署在 Windows/Linux PC 上，基于 Apache + PHP + MySQL 环境运行。
客户端 (Client)：Android 原生应用，通过 HTTP GET/POST 请求与服务端进行数据交互。
网络环境：所有设备需处于同一局域网（如 PC 接网线至路由器，移动端连接该路由器的 Wi-Fi）。

---

## ⚖️ 开源协议与声明 (License & Statement)

### 1. 协议说明
本项目采用 **Apache License 2.0** 协议授权。

### 2. 使用约束 (Usage Restrictions)
* **允许 (Allowed)**：你可以自由地下载、学习、修改并分享本项目的代码。
* **署名 (Attribution)**：在二次开发或分享时，必须注明原作者及本项目地址。
* **🚫 严禁盈利 (Strictly Non-Commercial)**：
    > **特别声明：本仓库所有代码仅供学习与技术交流使用。严禁任何个人或机构将本项目（包括但不限于源代码、编译后的 APK、修改版）用于任何形式的商业盈利活动，包括但不限于打包销售、集成至收费软件、插入广告获利等。**

---
