package com.zjh.client.view;

import com.zjh.common.Friend;

import javax.swing.*;
import java.util.List;

/**
 * @author 张俊鸿
 * @description: 好友列表页面
 * @since 2022-05-11 16:18
 */
public class FriendListView extends JFrame {
    public void showFriendList(List<Friend> allFriend){
        // TODO: 2022-05-11 后期上方会有表格类，调用方法刷新表格数据
        System.out.println("\n=========好友列表=========");
        for (Friend friend : allFriend) {
            System.out.println("【"+(friend.isOnLine() == true? "在线":"离线")+"】"+friend.getFriendId());
        }
    }
}
