package com.zjh.server.service.thread;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 管理服务器连接客户端线程 对登录的用户服务端线程进行统一管理
 * @author 张俊鸿
 * @date 2022/05/08
 **/
public class ManageServerConnectClientThread {
    //使用HashMap<K,V>进行统一管理,key就是用户id，保证用户同时在一个ip登录,Value是用户通信线程
    private static HashMap<String, ServerConnectClientThread> map = new HashMap<>();

    public static HashMap<String, ServerConnectClientThread> getMap() {
        return map;
    }

    /**
     * 添加线程
     *
     * @param userId                    用户id
     * @param clientConnectServerThread 客户端连接服务器线程
     *///加入用户线程
    public static void addThread(String userId, ServerConnectClientThread clientConnectServerThread){
        map.put(userId,clientConnectServerThread);
    }

    /**
     * 删除线程
     *
     * @param userId 用户id
     *///去除用户线程
    public static void removeThread(String userId){
        map.remove(userId);
    }

    /**
     * 获取线程
     *
     * @param userId 用户id
     * @return {@link ServerConnectClientThread}
     *///获取用户线程
    public static ServerConnectClientThread getThread(String userId){
        return map.get(userId);
    }

    /**
     * 返回在线用户列表
     *
     * @return {@link String}
     *///获取在线用户返回给客户端
    public static String returnOnlineUserList(){
        //遍历map返回
        Iterator<String> iterator = map.keySet().iterator();
        StringBuilder onlineUserList = new StringBuilder();
        while (iterator.hasNext()){
            onlineUserList.append(iterator.next() + " ");
        }
        return onlineUserList.toString();
    }

}
