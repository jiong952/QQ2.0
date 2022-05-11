package com.zjh.server.dao;

import com.zjh.common.Friend;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;

import static com.zjh.server.utils.MyDsUtils.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 张俊鸿
 * @description: 好友关系的db类
 * @since 2022-05-10 20:21
 */
public class FriendDao {
    public static void main(String[] args) {
//        System.out.println(new FriendDao().checkFriend("a", "zjh"));
//        new FriendDao().addFriend("s","a",true,new Date());
        System.out.println(new FriendDao().findAllFriend("a"));
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

    /**
     * 添加好友之前检查是否已经是好友
     *
     * @param myId 用户id
     * @param friendId 好友Id
     * @return boolean true表示好友
     */
    public boolean checkFriend(String myId,String friendId){
        boolean flag = false;
        String sql = "SELECT `id` FROM `friend` WHERE `my_id` = ? AND `friend_id` = ?";
        Object[] params = {myId,friendId};
        try {
            Map<String, Object> query = queryRunner.query(sql, new MapHandler(), params);
            if(query != null){
                //是好友
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 插入好友记录
     *
     * @param myId     用户
     * @param askerId 好友
     * @param isAsk    好友是否是发送好友请求的人
     * @param time     成为好友的时间
     * @return boolean
     */
    public boolean addFriend(String myId, String askerId, boolean isAsk, Date time){
        boolean flag = false;
        Object[] params = {myId,askerId,isAsk,time};
        String sql = "INSERT INTO `friend` (`my_id`,`friend_id`,`is_ask`,`create_time`) VALUES (?,?,?,?)";
        try {
            int update = queryRunner.update(sql, params);
            if(update > 0) flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

}
