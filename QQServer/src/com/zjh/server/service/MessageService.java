package com.zjh.server.service;

import com.zjh.common.Message;
import com.zjh.server.dao.MessageDao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张俊鸿
 * @description: 消息处理业务逻辑
 * @since 2022-05-12 20:32
 */
public class MessageService {
    private MessageDao messageDao = new MessageDao();
    /**
     * 插入消息记录
     *
     * @param msg     消息
     * @param isSuccess 成功
     * @return boolean
     */
    public boolean insertMsg(Message msg, boolean isSuccess){
        boolean flag = false;
        flag = messageDao.insertMsg(msg,isSuccess);
        return flag;
    }

    /**
     * 更新消息状态
     *
     * @param offLineMsg 离线消息
     * @return boolean
     */
    public boolean updateMsg(List<Message> offLineMsg){
        boolean flag = true;
        for (int i = 0; i < offLineMsg.size(); i++) {
            flag = messageDao.updateMsg(offLineMsg.get(i).getMsgId());
        }
        return flag;
    }
    /**
     * 私聊获取聊天记录
     *
     * @param myId     用户id
     * @param friendId 聊天朋友id
     * @return {@link List}<{@link Message}>
     */
    public List<Message> getAllMsg(String myId,String friendId){
        List<Message> list = new ArrayList<>();
        list = messageDao.getAllMsg(myId,friendId);
        return list;
    }
    /**
     * 获取用户的离线留言
     *
     * @param myId 用户id
     * @return {@link List}<{@link Message}>
     */
    public List<Message> getOffLineMsg(String myId){
        List<Message> list = new ArrayList<>();
        list = messageDao.getOffLineMsg(myId);
        //把消息改为success状态
        updateMsg(list);
        return list;
    }
}
