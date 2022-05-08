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

# 私聊功能

- 其实就是发送普通消息，但是需要服务器转发
- 注意弄清sender和getter
- 后期使用db可以将聊天内容存入数据库再实现离线留言功能

# 群发功能

- 本质还是私聊，就是发送对象改为所有人
- 后期升级离线留言
- 群聊：实质是群发，但是要在确定群组的前提下，这部分要用到数据库，或者用户手动选择创建群聊

# 群聊功能

- 没有数据库，只能每次选择要群聊的用户进行发送
- 本质是群发，只是对象是特定的而不是所有人

# 发送文件及文件下载功能

- 发送文件本质还是发送消息，这里我封装了一个fileMsg储存文件信息，加到Message属性中
  - 读取本地文件到字节数组
  - 封装
  - 发送
  - 服务端转发
- 接收文件
  - 这里本来想做客户端选择是否保存及保存的路径，但是会发生主线程及子线程同时阻塞争抢控制台，因此先摒弃，使用时间戳方式，后续图形化界面再加入该功能

# 服务端推送功能

- 本质也是群发
- 在服务端多开一条线程监听控制台输入

# 离线留言及发送文件
- 只需要修改服务端代码，增加一个暂存留言的集合，Key是userId，value是消息集合
- 服务端转发消息给客户端时进行判断，如果不在线则将消息暂存到map
- 用户登录后，服务端检查是否有留言，有就发送
