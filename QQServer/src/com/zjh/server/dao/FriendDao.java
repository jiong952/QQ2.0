package com.zjh.server.dao;

import com.zjh.common.Friend;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import static com.zjh.server.utils.MyDsUtils.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 张俊鸿
 * @description: 好友关系的db类
 * @since 2022-05-10 20:21
 */
public class FriendDao {
    public static void main(String[] args) {

    }


    /**
     * 获取好友列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link Friend}>
     */
    public List<Friend> findAllFriend(String userId){
        List<Friend> list = new ArrayList<>();
        String sql ="SELECT `friend_id` AS friendId,`is_ask` AS isAsk,`star` \n" +
                "AS star,`remark` AS remark,f.`create_time` AS `time`,\n" +
                "`user_name` AS friendName,`avatar` AS avatar,`gender` AS gender,\n" +
                "`age` AS age,`phone_number` AS phoneNumber\n" +
                "FROM `friend`  f\n" +
                "INNER JOIN `user` u\n" +
                "ON u.`user_id` = f.`my_id`\n" +
                "WHERE `my_id` = ?";
        try {
            list = queryRunner.query(sql, new BeanListHandler<>(Friend.class),userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("获取好友列表异常",e);
        }
        return list;
    }
}
