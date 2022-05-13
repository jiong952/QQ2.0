package com.zjh.server.service;

import com.zjh.common.Message;
import com.zjh.common.MessageType;
import com.zjh.server.dao.MessageDao;
import com.zjh.server.utils.FileUtils;

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
        //如果是文件的话，要从服务器本地读出
        for (Message msg : list) {
            if(MessageType.MESSAGE_FILE.equals(msg.getMsgType())){
                //如果是文件消息
                byte[] bytes = FileUtils.readFile(msg.getContent());
                msg.setFileBytes(bytes);
            }
        }
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
        //如果是文件的话，要从服务器本地读出
        for (Message msg : list) {
            if(MessageType.MESSAGE_FILE.equals(msg.getMsgType())){
                //如果是文件消息
                byte[] bytes = FileUtils.readFile(msg.getContent());
                msg.setFileBytes(bytes);
            }
        }
        //把消息改为success状态
        updateMsg(list);
        return list;
    }
    /**
     * 单方面删除消息记录
     * 单删是不会删除聊天记录的，回删就删除聊天记录
     *
     * @param myId     用户id
     * @param friendId 朋友id
     * @return boolean
     */
    public boolean updateDel(String myId,String friendId){
        boolean flag = false;
        flag = messageDao.updateDel(myId,friendId);
        return flag;
    }

}
