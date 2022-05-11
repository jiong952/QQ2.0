package com.zjh.client.view;

import com.zjh.client.service.UserService;
import com.zjh.common.User;
import com.zjh.utils.Utility;

import java.util.List;

/**
 * @author 张俊鸿
 * @description: 搜索用户界面
 * @since 2022-05-11 17:50
 */
public class SearchUserView {
    private UserService userService = new UserService();
    public void searchUserById(){
        System.out.println("\n=========搜索用户界面=========");
        System.out.print("请输入用户id：");
        String userId = Utility.readString(20);
        List<User> list = userService.searchUserById(userId);
        for (int i = 0; i < list.size(); i++) {
            // TODO: 2022-05-11 后期可加入头像，年龄
            System.out.println("【"+i+"】"+list.get(i).getUserId());
        }
    }
}
