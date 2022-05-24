package com.zjh.client.view.commandLine;

import com.zjh.client.service.MessageService;
import com.zjh.common.Message;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author 张俊鸿
 * @description: 聊天记录界面
 * @since 2022-05-13 0:22
 */
public class ChatHistoryView_old extends JFrame {
    private String userId;
    private String friendId;

    public ChatHistoryView_old(String userId, String friendId) throws HeadlessException {
        this.userId = userId;
        this.friendId = friendId;
    }

    /**
     * 显示聊天记录历史
     *
     */
    public void showHistory(){
        List<Message> list = new MessageService().getAllMsg(userId, friendId);
        for (Message message : list) {
            System.out.println("【"+message.getSendTime()+"】" + (message.getSenderId().equals(userId) ? "你" : message.getSenderId()) + "发送了：" +message.getContent());
        }
    }
}
