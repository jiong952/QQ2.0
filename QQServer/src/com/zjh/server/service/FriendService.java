package com.zjh.server.service;

import com.zjh.common.Friend;
import com.zjh.server.dao.FriendDao;
import com.zjh.server.dao.UserDao;

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
    public List<Friend> findAllFriend(String userId)  {
        List<Friend> list = null;
        List<String> friendIdList = friendDao.findAllFriendId(userId);
        list = userDao.findAllFriend(friendIdList);
        return list;
    }
}
