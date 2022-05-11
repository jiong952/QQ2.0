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
import java.util.Date;
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
//        System.out.println(new FriendService().findAllFriend("a"));
//        new FriendService().addFriend("a","bac",new Date());
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
    /**
     * 添加好友之前检查是否已经是好友
     *
     * @param myId 用户id
     * @param friendId 好友Id
     * @return boolean true表示好友
     */
    public boolean checkFriend(String myId,String friendId){
        boolean flag = false;
        flag = friendDao.checkFriend(myId,friendId);
        return flag;
    }

    /**
     * 往记录表中插入两条记录，区分申请和同意人
     *
     * @param myId    同意者
     * @param askerId 申请者
     * @param time    时间
     * @return boolean
     */
    public boolean addFriend(String myId, String askerId, Date time){
        boolean flag = false;
        //插入myId askerId isAsk = true time
        boolean b = friendDao.addFriend(myId, askerId, true, time);
        //插入askerId myId isAsk = false time
        boolean c = friendDao.addFriend(askerId, myId, false, time);
        if(b&&c) flag = true;
        return flag;
    }

}
