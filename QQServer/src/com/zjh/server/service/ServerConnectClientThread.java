package com.zjh.server.service;

import com.zjh.common.Message;
import com.zjh.common.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/** 服务端建立与客户端通讯线程 **/
public class ServerConnectClientThread extends Thread{
    //和客户端通讯的socket
    private Socket socket;
    private String userId;

    public ServerConnectClientThread( String userId,Socket socket) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    //接受或发送消息
    @Override
    public void run() {
        while (true){
            try {
                System.out.println("服务端通讯线程等待客户端发送的消息......");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果服务端没发送消息，线程会阻塞在这里
                Message msg = (Message)ois.readObject();
                if(MessageType.MESSAGE_GET_ONLINE_FRIEND.equals(msg.getMsgType())){
                    //客户端请求拉取在线用户列表
                    System.out.println(msg.getSender() + "请求在线用户列表");
                    //调用方法获得用户列表
                    String onlineUserList = ManageServerConnectClientThread.returnOnlineUserList();
                    System.out.println(onlineUserList);
                    //封装返回
                    Message msg_back = new Message();
                    msg_back.setContent(onlineUserList);
                    msg_back.setGetter(msg.getSender());
                    msg_back.setMsgType(MessageType.MESSAGE_RETURN_ONLINE_FRIEND);
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(msg_back);
                }else {
                    System.out.println("其他类型消息，暂不处理");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
