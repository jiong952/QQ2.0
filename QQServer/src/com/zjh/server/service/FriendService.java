package com.zjh.server.service;

import com.zjh.common.Friend;
import com.zjh.common.Message;
import com.zjh.common.MessageType;
import com.zjh.server.dao.FriendDao;
import com.zjh.server.dao.UserDao;
import com.zjh.server.service.thread.ManageServerConnectClientThread;
import com.zjh.server.service.thread.ServerThread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * @author 张俊鸿
 * @description: 服务端业务逻辑类
 * @since 2022-05-10 23:26
 */
public class FriendService {
    private UserDao userDao = new UserDao();
    private FriendDao friendDao = new FriendDao();

    public static void main(String[] args) {
        System.out.println(new FriendService().findAllFriend("a"));
    }

    /**
     * 获取好友列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link Friend}>
     */
    public List<Friend> findAllFriend(String userId)  {
        List<Friend> list = null;
        list = friendDao.findAllFriend(userId);
        //从在线的所有好友中进行过滤，设置friend的isOnline字段
        HashMap<String, ServerThread> map = ManageServerConnectClientThread.getMap();
        for (Friend friend : list) {
            if(map.get(friend.getFriendId()) != null){
                //在线 设置状态
                friend.setOnLine(true);
            }
        }
        return list;
    }

    /**
     * 通知好友该用户上线了
     *
     * @param userId 用户id
     */
    public void notifyOther(String userId){
        //给在线的好友发通知就行
        List<Friend> allFriend = findAllFriend(userId);
        for (Friend friend : allFriend) {
            if(friend.isOnLine()){
                //在线
                Message message = new Message();
                message.setMsgType(MessageType.NEW_ONLINE);
                message.setSenderId(userId);
                message.setGetterId(friend.getFriendId());

                //从在线池中拿出好友线程
                ServerThread thread = ManageServerConnectClientThread.getThread(friend.getFriendId());
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
