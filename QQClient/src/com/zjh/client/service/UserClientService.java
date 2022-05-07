package com.zjh.client.service;

import com.zjh.common.Message;
import com.zjh.common.MessageType;
import com.zjh.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

//参考mvc分层，这里做用户客户端的业务逻辑处理
public class UserClientService {

    private User u = new User();
    private Socket socket;

    //登录验证
    public String checkUser(String userId, String password){
        u.setUserId(userId);
        u.setPassword(password);
        //和服务器端取得联系，得到Socket
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            System.out.println("socket已连接...");
            //发送序列化用户对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);
            //接收服务端返回的消息
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message msg = (Message)ois.readObject();

            if(MessageType.message_succeed.equals(msg.getMsgType())){
                /*创建一个和服务端保持通信的线程*/
                ClientConnectServerThread connectServerThread = new ClientConnectServerThread(socket);
                //启动线程
                connectServerThread.start();
                //为了方便管理多用户，这里创建一个类进行统一管理
                ManageClientConnectServerThread.addThread(userId,connectServerThread);
                return MessageType.message_succeed;
            }else if(MessageType.message_login_fail.equals(msg.getMsgType())){
                //账号名或密码错误,关闭socket
                socket.close();
                return MessageType.message_login_fail;
            }else {
                //已登录,关闭socket
                socket.close();
                return MessageType.message_already_login;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return MessageType.message_login_fail;
    }
}
