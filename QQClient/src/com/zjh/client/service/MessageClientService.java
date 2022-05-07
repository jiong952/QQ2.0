package com.zjh.client.service;

import com.zjh.common.Message;
import com.zjh.common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**客户端聊天功能业务逻辑**/
public class MessageClientService {

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
