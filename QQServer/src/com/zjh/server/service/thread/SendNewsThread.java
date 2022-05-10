package com.zjh.server.service.thread;

import com.zjh.common.Message;
import com.zjh.common.MessageType;
import com.zjh.server.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 张俊鸿
 * @description: 服务端推送新闻的线程
 * @since 2022-05-08 17:11
 */
public class SendNewsThread extends Thread{
    @Override
    public void run() {
        while (true){
            //读取信息
            System.out.println("请输入要推送的新闻消息【输入q退出推送服务】：");
            String news = Utility.readString(50);
            if(("q").equals(news)){
                System.out.println("已退出推送服务");
                break;
            }
            //封装msg
            Message message = new Message();
            message.setSenderId("服务端");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date());
            message.setSendTime(time);
            message.setContent(news);
            message.setMsgType(MessageType.MESSAGE_NEWS);
            //从集合中拿到所有的在线用户【后期转化为全体用户】
            String onlineUserList = ManageServerConnectClientThread.returnOnlineUserList();
            String[] users = onlineUserList.split(" ");
            try {
                for (int i = 0; i < users.length; i++) {
                    ObjectOutputStream oos = new ObjectOutputStream(ManageServerConnectClientThread.getThread(users[i]).getSocket().getOutputStream());
                    oos.writeObject(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("【"+time+"】"+"已向所有人发送新闻：" +message.getContent());
        }
    }
}
