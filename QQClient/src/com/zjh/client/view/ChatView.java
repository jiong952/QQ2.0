package com.zjh.client.view;

import com.zjh.client.request.FriendRequest;
import com.zjh.client.service.MessageService;
import com.zjh.common.Message;
import com.zjh.utils.Utility;

import javax.swing.*;

/**
 * @author 张俊鸿
 * @description: 私聊聊天界面
 * @since 2022-05-12 1:38
 */
public class ChatView extends JFrame {
    private MessageService messageService = new MessageService();
    private FriendRequest friendRequest = new FriendRequest();
    private String userId;
    private String friendId;

    public ChatView(String userId, String friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    public void chat(){
        // TODO: 2022-05-12 这个方法之后删掉，改为button的触发事件
        System.out.println("\n========="+userId+"(我)与"+friendId+"的私聊界面=========");
        System.out.print("请输入发送的内容：");
        String chatContent = Utility.readString(100); //聊天内容
        //先验证一下是不是好友
        boolean b = friendRequest.checkFriend(userId, friendId);
        if(!b){
            //被单删了
            // TODO: 2022-05-12 在这个页面写一个弹窗 先设置为不可见
            System.out.println("对方不是你的好友，请先添加好友");
        }else {
            //调用一个MessageClientService的发送消息
            messageService.privateChat(chatContent,userId,friendId);
        }
    }

    /**
     * 发送消息或者收到消息，刷新页面
     *
     * @param message 消息
     */
    public void addMessage(Message message){
        // TODO: 2022-05-12 把消息加进去，重新刷新页面
        System.out.println("【"+message.getSendTime()+"】" + (message.getSenderId() == userId ? "你" : message.getSenderId()) + "发送了：" +message.getContent());
    }
}
