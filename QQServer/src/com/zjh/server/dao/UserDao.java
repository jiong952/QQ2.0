package com.zjh.server.dao;

import com.zjh.common.Friend;
import com.zjh.common.User;
import com.zjh.server.utils.JdbcUtils;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.Test;

import static com.zjh.server.utils.MyDsUtils.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 张俊鸿
 * @description: 用户校验，获取用户列表等db操作
 * @since 2022-05-10 16:28
 */
public class UserDao {
    //测试
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        System.out.println(new UserDao().findAllFriend(list));
    }

    public int register()  {
        int row = 0;
        User user = new User("12121","1");
        String sql = "INSERT INTO `user` (`user_id`,`password`,`user_name`) VALUES(?,?,?)";
        Object[] params = {user.getUserId(),user.getPassword(),user.getUserId()};
        try {
            row = queryRunner.execute(sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("注册异常",e);
        }
        return row;
    }

    /**
     * 登录验证
     *
     * @param userId 用户id
     * @return {@link User}
     */
    public User check(String userId){
        Connection conn;
        User user = new User();
        String sql = "SELECT `user_id` AS userId,`password` AS `password` FROM `user` WHERE `user_id`=?";
        Object[] params = {userId};
        try {
            user = queryRunner.query(sql,new BeanHandler<>(User.class),userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("登录失败",e);
        }
        return user;
    }

    /**
     * 根据好友id返回好友信息
     *
     * @param list 列表
     * @return {@link List}<{@link Friend}>
     */
    public List<Friend> findAllFriend(List<String> list){
        List<Friend> friendList = new ArrayList<>();
        for (String id : list) {
            String sql = "SELECT `user_id` AS friendId,`user_name` AS friendName,`avatar` AS avatar,`gender` AS gender,\n" +
                    "`age` AS age ,`phone_number` AS `phone_number`  FROM `user` WHERE `user_id`= ?";
            try {
                Friend query = queryRunner.query(sql, new BeanHandler<>(Friend.class), id);
                friendList.add(query);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("获取好友列表异常",e);
            }
        }
        return friendList;
    }
}
