package com.zjh.server.dao;

import com.zjh.common.Friend;
import com.zjh.common.User;
import com.zjh.server.utils.FileUtils;
import com.zjh.server.utils.JdbcUtils;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.junit.Test;

import static com.zjh.server.utils.MyDsUtils.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 张俊鸿
 * @description: 用户校验，获取用户列表等db操作
 * @since 2022-05-10 16:28
 */
public class UserDao {
    //测试
    public static void main(String[] args) {
//        ArrayList<String> list = new ArrayList<>();
//        list.add("a");
        List<User> list = new UserDao().searchUserById("a");
        for (User user : list) {
            System.out.println(user);
        }
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
        User user = new User();
        String sql = "SELECT `user_id` AS userId,`password` AS `password`,`user_name` AS userName,`avatar_path` AS avatarPath," +
                "`gender` AS gender,`age` AS age,`signature` AS signature FROM `user` WHERE `user_id`=?";
        Object[] params = {userId};
        try {
            user = queryRunner.query(sql,new BeanHandler<>(User.class),userId);
            //从本地读取头像文件
            user.setAvatar(FileUtils.readFile(user.getAvatarPath()));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("登录失败",e);
        }
        return user;
    }

    /**
     * 判断用户是否存在
     *
     * @param userId 用户id
     * @return boolean
     */
    public boolean isExist(String userId){
        boolean flag = false;
        String sql = "SELECT `user_id` FROM `user` WHERE `user_id`= ?";
        try {
            //key是字段名称，value是字段值
            Map<String, Object> query = queryRunner.query(sql, new MapHandler(), userId);
            if(query != null) flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 模糊查询到数据库搜索用户
     *
     * @param userId 用户id
     * @return {@link List}<{@link User}>
     */
    public List<User> searchUserById(String userId){
        List<User> list = new ArrayList<>();
        String sql = "SELECT  `user_id` AS userId," +
                "`user_name` AS userName,`avatar_path` AS avatarPath,`gender` AS gender,\n" +
                "`age` AS age,`signature` AS signature\n" +
                " FROM `user` WHERE `user_id` LIKE '%' ? '%'";
        try {
            list = queryRunner.query(sql, new BeanListHandler<>(User.class), userId);
            //遍历从本地读出头像
            for (User user : list) {
                user.setAvatar(FileUtils.readFile(user.getAvatarPath()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("搜索用户异常");
        }
        return list;
    }

    /**
     * 根据好友id返回好友信息
     * 暂时不用了
     * @param list 列表
     * @return {@link List}<{@link Friend}>
     */
    public List<Friend> findAllFriend(List<String> list){
        List<Friend> friendList = new ArrayList<>();
        for (String id : list) {
            String sql = "SELECT `user_id` AS friendId,`user_name` AS friendName,`avatar_path` AS avatarPath,`gender` AS gender,\n" +
                    "`age` AS age ,`signature` AS signature  FROM `user` WHERE `user_id`= ?";
            try {
                Friend query = queryRunner.query(sql, new BeanHandler<>(Friend.class), id);
                query.setAvatar(FileUtils.readFile(query.getAvatarPath()));
                friendList.add(query);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("获取好友列表异常",e);
            }
        }
        return friendList;
    }
}
