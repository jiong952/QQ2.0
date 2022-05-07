package com.zjh.server.service;

import java.util.HashMap;

/** 对登录的用户服务端线程进行统一管理 **/
public class ManageServerConnectClientThread {
    //使用HashMap<K,V>进行统一管理,key就是用户id，保证用户同时在一个ip登录,Value是用户通信线程
    private static HashMap<String, ServerConnectClientThread> map = new HashMap<>();

    //加入用户线程
    public static void addThread(String userId, ServerConnectClientThread clientConnectServerThread){
        map.put(userId,clientConnectServerThread);
    }

    //获取用户线程
    public static ServerConnectClientThread getThread(String userId){
        return map.get(userId);
    }
}
