package com.zjh.client.manage;

import com.zjh.client.view.ChatView;
import com.zjh.client.view.FriendsVerifyView;

import java.util.HashMap;

/**
 * @author 张俊鸿
 * @description: 好友验证界面管理
 * @since 2022-05-25 3:49
 */
public class ManageFriendsVerifyView {
    private static HashMap<String, FriendsVerifyView> map = new HashMap<>();
    public static void addView(String friendId,FriendsVerifyView friendsVerifyView){
        map.put(friendId,friendsVerifyView);
    }
    public static FriendsVerifyView getView(String userId){
        return map.get(userId);
    }
}
