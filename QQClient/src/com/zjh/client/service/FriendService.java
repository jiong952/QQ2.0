package com.zjh.client.service;

import com.zjh.common.Friend;
import com.zjh.common.RequestMsg;
import com.zjh.common.ResponseMsg;
import com.zjh.common.StateCode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

/**
 * @author 张俊鸿
 * @description: 好友管理业务逻辑类
 * @since 2022-05-10 22:56
 */
public class FriendService {
    public static void main(String[] args) {
        List<Friend> a = new FriendService().findAllFriend("a");
        System.out.println(a);
    }
    private Socket socket;

    /**
     * 向服务器请求好友列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link Friend}>
     */
    public List<Friend> findAllFriend(String userId){
        List<Friend> list = null;
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9998);
            System.out.println("用户请求拉取");
            //发送序列化用户对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            RequestMsg requestMsg = new RequestMsg();
            //方法名和参数
            requestMsg.setRequesterId(userId);
            requestMsg.setContent("findAllFriend");
            requestMsg.setParams(new Object[]{userId});
            oos.writeObject(requestMsg);
            //接收服务端响应的消息
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ResponseMsg responseMsg = (ResponseMsg) ois.readObject();
            list = (List<Friend>) responseMsg.getReturnValue();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}
