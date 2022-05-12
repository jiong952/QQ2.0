package com.zjh.client.manage;

import com.zjh.client.view.ChatHistoryView;
import com.zjh.client.view.ChatView;

import java.util.HashMap;

/**
 * @author 张俊鸿
 * @description: 管理查找聊天记录的界面
 * @since 2022-05-13 0:23
 */
public class ManageChatHistoryView {
    //使用使用HashMap<K,V>进行统一管理,key就是friendId，保证同时只能打开一个聊天界面，Value是ChatView
    private static HashMap<String, ChatHistoryView> map = new HashMap<>();

    /**
     * 添加聊天界面
     *
     * @param friendId 朋友id
     * @param chatHistoryView 聊天观点
     */
    public static void addView(String friendId,ChatHistoryView chatHistoryView){
        map.put(friendId,chatHistoryView);
    }

    /**
     * 得到唯一聊天界面
     *
     * @param friendId 朋友id
     * @return {@link ChatView}
     */
    public static ChatHistoryView getView(String friendId){
        return map.get(friendId);
    }
}
