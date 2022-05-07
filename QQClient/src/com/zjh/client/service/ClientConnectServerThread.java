package com.zjh.client.service;

import com.zjh.common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/** 客户端通讯线程 **/
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
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
