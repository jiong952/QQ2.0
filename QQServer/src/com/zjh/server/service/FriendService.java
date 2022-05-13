package com.zjh.server.service;

import com.zjh.common.Friend;
import com.zjh.common.Message;
import com.zjh.common.MessageType;
import com.zjh.server.dao.FriendDao;
import com.zjh.server.dao.MessageDao;
import com.zjh.server.dao.UserDao;
import com.zjh.server.manage.ManageServerConnectClientThread;
import com.zjh.server.thread.ServerThread;

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
    private MessageDao messageDao = new MessageDao();

    public static void main(String[] args) {
//        System.out.println(new FriendService().findAllFriend("a"));
//        new FriendService().addFriend("a","bac",new Date());
//        System.out.println(new FriendService().deleteFriend("s", "jj"));
        System.out.println(new FriendService().updateFriend("admin", "a", "这是好人", true));
    }

    /**
     * 获取好友列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link Friend}>
     */
    public List<Friend> findAllFriend(String userId)  {
        List<Friend> list = null;
        //正常好友
        list = friendDao.findAllFriend(userId);
        List<String> ids = friendDao.getDelMeFriend(userId);
        List<Friend> delMeFriends = userDao.findAllFriend(ids);
        //从在线的所有好友中进行过滤，设置friend的isOnline字段
        HashMap<String, ServerThread> map = ManageServerConnectClientThread.getMap();
        for (Friend friend : list) {
            if(map.get(friend.getFriendId()) != null){
                //在线 设置状态
                friend.setOnLine(true);
            }
        }
        //被删除的人还是可以看到好友的，这是不能发送消息
        list.addAll(delMeFriends);
        return list;
    }

    /**
     * 通知好友该用户上线了
     *
     * @param userId 用户id
     */
    public void notifyOther(String userId,String msgType){
        //给在线的好友发通知就行
        List<Friend> allFriend = findAllFriend(userId);
        for (Friend friend : allFriend) {
            if(friend.isOnLine()){
                //在线
                Message message = new Message();
                message.setMsgType(msgType);
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

    /**
     * 删除朋友
     *
     * @param myId     用户id
     * @param friendId 朋友id
     * @return boolean
     */
    public boolean deleteFriend(String myId,String friendId){
        boolean flag = false;
        //判断是好友,是好友则是单删，不是好友则是删回去
        boolean b2 = checkFriend(myId, friendId);
        if(b2){
            //删除两条好友记录
            boolean b = friendDao.deleteFriendRecord(myId, friendId);
            boolean b1 = friendDao.deleteFriendRecord(friendId, myId);
            //插入一条删除记录
            boolean c = friendDao.insertDelFriendRecord(myId, friendId);
            //把消息表中对应消息的字段改为1
            messageDao.updateDel(myId, friendId);
            if(b&&b1&&c) flag = true;
        }else {
            //删除掉删除记录，彻底删除好友关系
            boolean b = friendDao.delDelFriendRecord(friendId, myId);
            //清空消息记录
            messageDao.clearMsg(myId, friendId);
            if(b) flag = true;
        }
        return flag;
    }

    /**
     * 更新对朋友的备注以及星标
     *
     * @param userId   用户id
     * @param friendId 朋友id
     * @param remark   备注
     * @param star     明星
     * @return boolean
     */
    public boolean updateFriend(String userId,String friendId,String remark,Boolean star){
        // TODO: 2022-05-12 可以拓展分组
        boolean flag = false;
        Friend friend = new Friend();
        friend.setFriendId(friendId);
        friend.setRemark(remark);
        friend.setStar(star);
        flag = friendDao.updateFriend(userId,friend);
        return flag;
    }
}
