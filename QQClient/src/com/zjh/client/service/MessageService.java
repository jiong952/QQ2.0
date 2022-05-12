package com.zjh.client.service;

import com.zjh.client.manage.ManageClientConnectServerThread;
import com.zjh.common.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 消息客户端服务客户端聊天功能业务逻辑
 * @author 张俊鸿
 * @date 2022/05/08
 **/


public class MessageService {

    private Socket socket;
    /**
     * 群聊功能，选择指定好友发送
     *
     * @param content 内容
     * @param sender  发送方
     * @param getters 接收方
     */
    public void groupChat(String content,String sender,String getters){
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_GROUP_CHAT);
        message.setSenderId(sender);
        message.setGetterId(getters);
        message.setContent(content);
        //Sun May 08 01:11:07 CST 2022
        //时间后期转化为正常格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = sdf.format(date);
        message.setSendTime(date);
        System.out.println("【"+time+"】 你对" + getters + "发送了：" +content);
        //从用户集合中拿到当前通讯进程，发送该消息
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 群发功能 发送给自己的所有好友 【后期拓展离线留言】
     * @param chatContent 聊天内容
     * @param senderId    发件人id
     */
    public void sendMsgToAll(String chatContent,String senderId){
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_TO_ALL_MSG);
        message.setSenderId(senderId);
        message.setContent(chatContent);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = sdf.format(date);
        message.setSendTime(date);
        //从用户集合中拿到当前通讯进程，发送该消息
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 私人聊天
     *
     * @param chatContent 聊天内容
     * @param senderId    发件人id
     * @param getterId    getter id
     */
    //私聊功能
    public void privateChat(String chatContent,String senderId,String getterId){
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_COMMON_MSG);
        message.setSenderId(senderId);
        message.setGetterId(getterId);
        message.setContent(chatContent);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = sdf.format(date);
        message.setSendTime(date);
//        System.out.println("【"+time+"】 你对" + getterId + "发送了：" +chatContent);
        //从用户集合中拿到当前通讯进程，发送该消息
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getThread(senderId).getSocket().getOutputStream());
            System.out.println("私聊" + message);
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 私聊获取聊天记录 普通消息 文件消息 群发消息
     *
     * @param myId     用户id
     * @param friendId 聊天朋友id
     * @return {@link List}<{@link Message}>
     */
    public List<Message> getAllMsg(String myId,String friendId){
        List<Message> list = new ArrayList<>();
        try {
            socket = new Socket(InetAddress.getByName(StaticString.server_ip), StaticString.server_port);
            //发送序列化用户对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            RequestMsg requestMsg = new RequestMsg();
            //方法名和参数
            requestMsg.setRequesterId(myId);
            requestMsg.setContent("getAllMsg");
            requestMsg.setParams(new Object[]{friendId});
            oos.writeObject(requestMsg);
            //接收服务端响应的消息
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ResponseMsg responseMsg = (ResponseMsg) ois.readObject();
            list = (List<Message>) responseMsg.getReturnValue();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

}
