package com.zjh.client.manage;

import com.zjh.client.thread.ClientConnectServerThread;
import com.zjh.common.User;

import java.util.HashMap;

/**
 * @author 张俊鸿
 * @description: 用户信息管理类
 * @since 2022-05-22 13:00
 */
public class ManageUser {
    private static HashMap<String, User> map = new HashMap<>();
    public static void addUser(String userId,User user){
        map.put(userId,user);
    }
    public static User getUser(String userId){
        return map.get(userId);
    }
}
