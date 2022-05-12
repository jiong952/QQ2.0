package com.zjh.client.view;

import com.zjh.client.request.FriendRequest;
import com.zjh.common.Friend;
import com.zjh.utils.Utility;

import javax.swing.*;

/**
 * @author 张俊鸿
 * @description: 修改对好友信息的页面
 * @since 2022-05-12 1:22
 */
public class UpdateFriendView extends JFrame {
    private FriendRequest friendRequest = new FriendRequest();
    private String userId;

    public UpdateFriendView(String userId) {
        this.userId = userId;
    }

    public void update(Friend friend){
        System.out.println("========好友信息========");
        System.out.println(friend);
        // TODO: 2022-05-12 所有能显示的信息都显示出来，但是只能改备注【文本框】和星标【单选框】
        System.out.println("请输入备注");
        String remark = Utility.readString(50);
        System.out.println("是否设为星标(1/0)");
        int i = Utility.readInt(1);
        Boolean star = false;
        if(i == 1) star = true;
        boolean b = friendRequest.updateFriend(userId, friend.getFriendId(), remark, star);
        if(b){
            System.out.println("更新成功");
        }else {
            System.out.println("更新失败");
        }
    }
}
