package com.zjh.client.view;

import com.zjh.client.request.FriendRequest;

import javax.swing.*;

/**
 * @author 张俊鸿
 * @description: 好友验证界面
 * @since 2022-05-11 22:16
 */
@SuppressWarnings("all")
public class FriendsVerifyView extends JFrame {
    private FriendRequest friendRequest = new FriendRequest();

    public void addVerifyRecord(String askerId, String myId){
        // TODO: 2022-05-11 将记录加到界面中
        System.out.println("\n=========好友验证界面=========");
        System.out.println("【"+askerId+"】请求添加您为好友");
        // TODO: 2022-05-11 由于输入阻塞问题，这里默认同意，到时候用button来调用方法传参
        System.out.println("同意");
        friendRequest.permitMakeFriend(myId,askerId);
    }
}
