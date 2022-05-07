package com.zjh.server.service;

import com.zjh.common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
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
                System.out.println("服务端通讯线程等待客户端端发送的消息......");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果服务端没发送消息，线程会阻塞在这里
                Message msg = (Message)ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
