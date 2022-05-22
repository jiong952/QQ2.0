package com.zjh.client.view;

import com.zjh.client.manage.ManageUser;
import com.zjh.common.Friend;
import com.zjh.common.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author 张俊鸿
 * @description: 好友列表页面
 * @since 2022-05-11 16:18
 */
public class MyQQView extends JFrame {
    //当前用户的id
    private String userId;

    public MyQQView(String userId){
        this.userId = userId;
        //在管理用户信息类中拿到user全部信息
        User user = ManageUser.getUser(userId);
        System.out.println(user);
    }

    public void showFriendList(List<Friend> allFriend){
        // TODO: 2022-05-11 后期上方会有表格类，调用方法刷新表格数据
        System.out.println("\n=========好友列表=========");
        for (Friend friend : allFriend) {
            System.out.println("【"+(friend.isOnLine() == true? "在线":"离线")+"】"+friend.getFriendId());
        }
    }
}
