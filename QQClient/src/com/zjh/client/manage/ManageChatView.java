package com.zjh.client.manage;

import com.zjh.client.view.ChatView;

import java.util.HashMap;

/**
 * @author 
 * @description: asd
 * @since 2022-05-12 18:40
 */
public class ManageChatView {
    //使用使用HashMap<K,V>进行统一管理,key就是friendId，保证同时只能打开一个聊天界面，Value是ChatView
    private static HashMap<String, ChatView> map = new HashMap<>();

    /**
     * 添加聊天界面
     *
     * @param friendId 朋友id
     * @param chatView 聊天观点
     */
    public static void addView(String friendId,ChatView chatView){
        map.put(friendId,chatView);
    }

    /**
     * 得到唯一聊天界面
     *
     * @param friendId 朋友id
     * @return {@link ChatView}
     */
    public static ChatView getView(String friendId){
        return map.get(friendId);
    }
}
