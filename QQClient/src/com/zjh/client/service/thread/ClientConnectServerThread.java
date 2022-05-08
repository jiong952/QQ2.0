package com.zjh.client.service.thread;

import com.zjh.common.Message;
import com.zjh.common.MessageType;
import com.zjh.utils.FileUtils;
import com.zjh.utils.Utility;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Date;

/**
 * 客户端连接服务器线程 客户端通讯线程
 * @author 张俊鸿
 * @date 2022/05/08
 **/


public class ClientConnectServerThread extends Thread{
    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    //接受或发送消息
    @Override
    public void run() {
        while (true){
            try {
                System.out.println("客户端通讯线程等待服务端发送的消息......");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果服务端没发送消息，线程会阻塞在这里
                Message msg = (Message)ois.readObject();
                //服务端返回用户列表
                if(MessageType.MESSAGE_RETURN_ONLINE_FRIEND.equals(msg.getMsgType())){
                    //取出用户列表进行展示,真正项目其实是用json前后端交互，这里用String简便处理
                    String[] onlineUsers = msg.getContent().split(" ");
                    System.out.println("========当前在线用户列表========");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("用户：" + onlineUsers[i]);
                    }
                }else if(MessageType.MESSAGE_COMMON_MSG.equals(msg.getMsgType()) || MessageType.MESSAGE_TO_ALL_MSG.equals(msg.getMsgType())){
                    //私聊和群发的本质是一样的
                    System.out.println("\n=========与"+msg.getSender()+"的私聊界面=========");
                    //拿到普通消息
                    System.out.println("【"+msg.getSendTime()+"】"+msg.getSender()+"对你发送了：" +msg.getContent());
                }else if(MessageType.MESSAGE_GROUP_CHAT.equals(msg.getMsgType())){
                    //群聊功能
                    System.out.println("\n========="+msg.getSender() +" "+msg.getGetter()+"的群聊界面=========");
                    System.out.println("【"+msg.getSendTime()+"】"+msg.getSender()+"发送了：" +msg.getContent());
                }else if(MessageType.MESSAGE_FILE.equals(msg.getMsgType())){
                    //发送文件过来
                    System.out.println("\n=========与"+msg.getSender()+"的私聊界面=========");
                    //拿到文件信息
                    System.out.println("【"+msg.getSendTime()+"】"+msg.getSender()+"对你发送了：" +msg.getFileMsg().getFileName());
                    //用户选择存放路径 这里无法选择路径，因为子线程会和主线程同时阻塞等待scanner，发生冲突
                    //但后期使用图形化界面就不会同时争夺同一个资源（文件管理器）
                    //先用封装替代
                    String desc = "D:\\" + msg.getGetter() +"_" + new Date().getTime() + "_" + msg.getFileMsg().getFileName();
                    FileUtils.storeFile(msg.getFileMsg().getFileBytes(),desc);
                    System.out.println(msg.getFileMsg().getFileName()+ " 已保存到" + desc);
                }else if(MessageType.MESSAGE_NEWS.equals(msg.getMsgType())){
                    //服务端推送的消息
                    System.out.println("\n=========服务端推送界面=========");
                    System.out.println("【"+msg.getSendTime()+"】"+msg.getSender()+"对你发送了：" +msg.getContent());
                }else {
                    System.out.println("其他类型msg，暂时不处理");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
