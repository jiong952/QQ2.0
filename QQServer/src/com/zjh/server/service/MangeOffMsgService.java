package com.zjh.server.service;

import com.zjh.common.Message;
import com.zjh.common.User;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 张俊鸿
 * @description: 管理离线消息的业务逻辑
 * @since 2022-05-08 18:45
 */
public class MangeOffMsgService {

    /**
     * 所有用户的离线消息集合
     * String 用户id
     * ArrayList<Message> 消息集合
     */
    private static ConcurrentHashMap<String, ArrayList<Message>> offMsgMap = new ConcurrentHashMap<>();

    /**
     * 获得map
     *
     * @return {@link ConcurrentHashMap}<{@link String}, {@link ArrayList}<{@link Message}>>
     */
    public static ConcurrentHashMap<String, ArrayList<Message>> getOffMsgMap() {
        return offMsgMap;
    }

    /**
     * 添加离线消息
     *
     * @param getter  getter
     * @param message 消息
     */
    public static void addOffMsg(String getter,Message message){
        if(offMsgMap.get(getter) == null){
            offMsgMap.put(getter,new ArrayList<Message>());
        }
        ArrayList<Message> messageList = offMsgMap.get(getter);
        messageList.add(message);
    }

    /**
     * 移除离线消息用户
     *
     * @param getter getter
     */
    public static void removeOffMsg(String getter){
        offMsgMap.remove(getter);
    }
}
