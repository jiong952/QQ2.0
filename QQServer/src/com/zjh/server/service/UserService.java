package com.zjh.server.service;

import com.zjh.common.User;
import com.zjh.server.dao.UserDao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张俊鸿
 * @description: 用户操作业务逻辑
 * @since 2022-05-11 17:41
 */
public class UserService {
    private UserDao userDao = new UserDao();
    /**
     * 用户登录验证
     *
     * @param userId   用户id
     * @param password 密码
     * @return boolean
     */
    public User checkUser(String userId, String password){
        User check = userDao.check(userId);
        if(check != null && userId.equals(check.getUserId()) && password.equals(check.getPassword())) return check;
        return null;
    }

    /**
     * 模糊查询到数据库搜索用户
     *
     * @param userId 用户id
     * @return {@link List}<{@link User}>
     */
    public List<User> searchUserById(String userId){
        List<User> list = new ArrayList<>();
        list = userDao.searchUserById(userId);
        return list;
    }
}
