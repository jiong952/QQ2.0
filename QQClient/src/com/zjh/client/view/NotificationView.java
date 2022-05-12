package com.zjh.client.view;

import com.zjh.common.Friend;

import javax.swing.*;

/**
 * @author 张俊鸿
 * @description: 消息弹窗界面
 * @since 2022-05-11 16:15
 */
public class NotificationView extends JFrame {
    // TODO: 2022-05-11 做个弹窗，后台一调用就弹出某某某好友已上线
    public void onLineRemind(String friendId){
        System.out.println(friendId + "已上线！");
    }

    public void askMakeFriendSuccess(String permitterId){

        System.out.println(permitterId + "已同意你的好友请求，赶快开始聊天吧~");

    }
    public void permitMakeFriendSuccess(String askerId){
        System.out.println(askerId + "已是你的好友，赶快开始聊天吧~");
    }
}
