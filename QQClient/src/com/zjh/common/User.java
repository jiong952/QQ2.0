package com.zjh.common;

import java.io.Serializable;

/**
 * 用户
 *
 * @author 张俊鸿
 * @date 2022/05/08
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userId;
    private String password;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {
    }
}
