package com.zjh.server.dao;

import com.zjh.common.Friend;
import com.zjh.common.FriendShip;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.junit.Test;

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
        System.out.println(new FriendDao().findAllFriendId("a"));
    }

    /**
     * 返回所有好友
     *
     * @param userId 用户id
     * @return {@link List}<{@link String}>
     */
    public List<String> findAllFriendId(String userId)  {
        List<String> list = new ArrayList<>();
        List<FriendShip> friendShips;
        Object[] params ={userId,userId};
        //返回所有好友
        String sql ="SELECT `asker_id`  AS askerId , `permitter_id`  AS permitterId  FROM `friend_rel` WHERE `asker_id` = ? OR `permitter_id` = ?";
        try {
            friendShips=queryRunner.query(sql, new BeanListHandler<>(FriendShip.class),params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("获取好友列表id",e);
        }
        if(friendShips != null){
            for (FriendShip friendShip : friendShips) {
                if(userId.equals(friendShip.getAskerId())){
                    list.add(friendShip.getPermitterId());
                }else if(userId.equals(friendShip.getPermitterId())){
                    list.add(friendShip.getAskerId());
                }
            }
        }
        return list;
    }
}
