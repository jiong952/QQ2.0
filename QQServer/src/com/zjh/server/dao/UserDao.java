package com.zjh.server.dao;

import com.zjh.common.User;
import com.zjh.server.utils.JdbcUtils;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.Test;

import static com.zjh.server.utils.MyDsUtils.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author 张俊鸿
 * @description: 用户校验，获取用户列表等db操作
 * @since 2022-05-10 16:28
 */
public class UserDao {

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
}
