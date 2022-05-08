package com.zjh.client.service;

import com.zjh.common.Message;
import com.zjh.common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**客户端聊天功能业务逻辑**/
public class MessageClientService {

    //群聊功能，选择指定好友发送
    public void groupChat(String content,String sender,String getters){
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_GROUP_CHAT);
        message.setSender(sender);
        message.setGetter(getters);
        message.setContent(content);
        //Sun May 08 01:11:07 CST 2022
        //时间后期转化为正常格式
        String time = new Date().toString();
        message.setSendTime(time);
        System.out.println("【"+time+"】 你对" + getters + "发送了：" +content);
        //从用户集合中拿到当前通讯进程，发送该消息
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //群发功能 发送给自己的所有好友 【后期拓展离线留言】
    public void sendMsgToAll(String chatContent,String senderId){
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_TO_ALL_MSG);
        message.setSender(senderId);
        message.setContent(chatContent);
        //Sun May 08 01:11:07 CST 2022
        //时间后期转化为正常格式
        String time = new Date().toString();
        message.setSendTime(time);
        System.out.println("【"+time+"】 你对所有人发送了：" +chatContent);
        //从用户集合中拿到当前通讯进程，发送该消息
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //私聊功能
    public void privateChat(String chatContent,String senderId,String getterId){
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_COMMON_MSG);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(chatContent);
        //Sun May 08 01:11:07 CST 2022
        //时间后期转化为正常格式
        String time = new Date().toString();
        message.setSendTime(time);
        System.out.println("【"+time+"】 你对" + getterId + "发送了：" +chatContent);
        //从用户集合中拿到当前通讯进程，发送该消息
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
