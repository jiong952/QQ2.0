package com.zjh.common;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

/**
 * @author 张俊鸿
 * @description: 好友类
 * @since 2022-05-10 20:03
 */
public class Friend implements Serializable {
    private static final long serialVersionUID = 1L;
    /**账号**/
    private String friendId;
    /**昵称**/
    private String friendName;
    /**在线状态**/
    private boolean onLine;
    /**头像**/
    private byte[] avatar;
    /**头像路径**/
    private String avatarPath;
    /**性别 默认2 男1 女0**/
    private int gender;
    /**年龄**/
    private int age;
    /**个性签名**/
    private String signature;
    /**备注**/
    private String remark;
    /**这个好友是否是发送好友申请的人**/
    private boolean isAsk;
    /**表示是否是星标好友**/
    private boolean star;
    /**分组:后续功能运行用户设置分组，可拓展为权限，某发言仅分组可见
     * 分组可有多个
     * 暂定组id，后续可加一个组类**/
    private Set<String> group;
    /**成为好友的时间**/
    private Date time;


    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "friendId='" + friendId + '\'' +
                ", friendName='" + friendName + '\'' +
                ", onLine=" + onLine +
                ", avatar=" + Arrays.toString(avatar) +
                ", avatarPath='" + avatarPath + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", signature='" + signature + '\'' +
                ", remark='" + remark + '\'' +
                ", isAsk=" + isAsk +
                ", star=" + star +
                ", group=" + group +
                ", time=" + time +
                '}';
    }

    public Friend(String friendId, String friendName, boolean onLine, byte[] avatar, String avatarPath, int gender, int age, String signature, String remark, boolean isAsk, boolean star, Set<String> group, Date time) {
        this.friendId = friendId;
        this.friendName = friendName;
        this.onLine = onLine;
        this.avatar = avatar;
        this.avatarPath = avatarPath;
        this.gender = gender;
        this.age = age;
        this.signature = signature;
        this.remark = remark;
        this.isAsk = isAsk;
        this.star = star;
        this.group = group;
        this.time = time;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Friend() {
    }

    public boolean isAsk() {
        return isAsk;
    }

    public void setAsk(boolean ask) {
        isAsk = ask;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public Set<String> getGroup() {
        return group;
    }

    public void setGroup(Set<String> group) {
        this.group = group;
    }
}
