package com.zjh.client.view;

import com.zjh.client.service.MessageClientService;
import com.zjh.utils.Utility;

/**
 * @author 张俊鸿
 * @description: 私聊聊天界面
 * @since 2022-05-12 1:38
 */
public class ChatView {
    private MessageClientService messageClientService = new MessageClientService();
    private String userId;
    private String friendId;

    public ChatView(String userId, String friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    public void chat(){
        System.out.println("\n========="+userId+"(我)与"+friendId+"的私聊界面=========");
        System.out.print("请输入发送的内容：");
        String chatContent = Utility.readString(100); //聊天内容
        //调用一个MessageClientService的发送消息
        messageClientService.privateChat(chatContent,userId,friendId);
    }
}
