# 写在前面

课程链接：[【韩顺平讲Java】Java网络多线程专题 - TCP UDP Socket编程 多线程 并发处理 文件传输 新闻推送 Java_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1j54y1b7qv?p=1)

之前没做过类似项目，这次结合多线程 + io + 网络编程做个项目练练手

# 1.0概述

- 使用命令行界面
- 不使用数据库
- 分为Client和Server两个项目

# 登录概述

- 实现多用户登录
- 实现用户单点登录 不可重复登录

# 拉取功能概述
- 通过服务端ManageServerConnectClientThread管理的用户登录线程map返回在线用户给客户端
- 两边不断在通讯线程的run方法中通信，根据Message类型不同调用不同方法

# 用户安全退出

- 客户端使用System.exit(0);退出整个进程，包括与服务端的通信进程
- 客户端发送退出消息给服务端，服务端从用户集合中去除客户端同时break推出线程run方法
