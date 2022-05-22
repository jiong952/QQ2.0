package com.zjh.common;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 用户
 *
 * @author 张俊鸿
 * @date 2022/05/08
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    /**账号**/
    private String userId;
    /**密码**/
    private String password;
    /**昵称**/
    private String userName;
    /**在线状态**/
    private boolean onLine;
    /**头像**/
    private byte[] avatar;
    /**头像存储路径**/
    private String avatarPath;
    /**性别 默认2 男1 女0**/
    private int gender;
    /**年龄**/
    private int age;
    /**个性签名**/
    private String signature;

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isOnLine() {
        return onLine;
    }

    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public User() {
    }

    public User(String userId, String password, String userName, boolean onLine, byte[] avatar, String avatarPath, int gender, int age, String signature) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.onLine = onLine;
        this.avatar = avatar;
        this.avatarPath = avatarPath;
        this.gender = gender;
        this.age = age;
        this.signature = signature;
    }

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", onLine=" + onLine +
                ", avatar=" + Arrays.toString(avatar) +
                ", avatarPath='" + avatarPath + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", signature='" + signature + '\'' +
                '}';
    }
}
