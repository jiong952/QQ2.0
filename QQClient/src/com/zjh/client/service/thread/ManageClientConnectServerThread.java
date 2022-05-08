package com.zjh.client.service.thread;

import java.util.HashMap;

/**
 * 管理客户端连接服务器线程 对登录的用户客户端线程进行统一管理
 * @author 张俊鸿
 * @date 2022/05/08
 **/
public class ManageClientConnectServerThread {
    //使用HashMap<K,V>进行统一管理,key就是用户id，保证用户同时在一个ip登录,Value是用户通信线程
    private static HashMap<String,ClientConnectServerThread> map = new HashMap<>();

    //加入用户线程
    public static void addThread(String userId, ClientConnectServerThread clientConnectServerThread){
        map.put(userId,clientConnectServerThread);
    }

    //获取用户线程
    public static ClientConnectServerThread getThread(String userId){
        return map.get(userId);
    }
}
