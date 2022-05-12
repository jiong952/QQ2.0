package com.zjh.client.view;

import com.zjh.client.request.FriendRequest;
import com.zjh.client.request.UserRequest;
import com.zjh.common.User;
import com.zjh.utils.Utility;

import javax.swing.*;
import java.util.List;

/**
 * @author 张俊鸿
 * @description: 搜索用户界面
 * @since 2022-05-11 17:50
 */
public class SearchUserView extends JFrame {
    private String myId;

    public SearchUserView(String myId) {
        this.myId = myId;
    }

    private UserRequest userRequest = new UserRequest();
    private FriendRequest friendRequest = new FriendRequest();
    public void searchUserById(){
        System.out.println("\n=========搜索用户界面=========");
        System.out.print("请输入用户id：");
        String userId = Utility.readString(20);
        List<User> list = userRequest.searchUserById(myId,userId);
        for (int i = 0; i < list.size(); i++) {
            // TODO: 2022-05-11 后期可加入头像，年龄
            System.out.println("【"+i+"】"+list.get(i).getUserId());
        }
        System.out.println("\n===========================");
        System.out.println("请输入你要添加的好友id【以上范围的】:");
        // TODO: 2022-05-11 到时候用按钮形式，点击就传参调用方法
        String addId = Utility.readString(20);
        // TODO: 2022-05-11 检查是不是好友
        boolean b = friendRequest.checkFriend(myId, addId);
        if(!b){
            //不是好友
            friendRequest.askMakeFriend(myId,addId);
        }else {
            // TODO: 2022-05-11 在用户卡片加一行红字:你们已经是好友！
            System.out.println(addId+"已经是您的好友了！不可重复添加");
        }
    }
}
