package com.zjh.server.dao;

import com.zjh.common.Friend;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.junit.Test;

import static com.zjh.server.utils.MyDsUtils.*;
import java.sql.SQLException;
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
     * 返回所有好友
     *
     * @param userId 用户id
     * @return {@link List}<{@link Friend}>
     */
    public List<Friend> findAll(String userId)  {
        List<Friend> list;
        //返回所有好友
        String sql ="SELECT `asker_id`  AS askerId , `permitter_id`  AS permitterId  FROM `friend_rel` WHERE `asker_id` = 'a' OR `permitter_id` = ?";
        try {
            list=queryRunner.query(sql, new ColumnListHandler<>(),userId);
            System.out.println(list);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("查看点赞合集异常",e);
        }
        return list;
    }
}
