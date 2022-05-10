package com.zjh.common;

/**
 * @author 张俊鸿
 * @description: 好友关系，用于数据库处理出来
 * @since 2022-05-10 23:43
 */
@SuppressWarnings("all")
public class FriendShip {
    private String askerId;
    private String permitterId;

    @Override
    public String toString() {
        return "FriendShip{" +
                "askerId='" + askerId + '\'' +
                ", permitterId='" + permitterId + '\'' +
                '}';
    }

    public String getAskerId() {
        return askerId;
    }

    public void setAskerId(String askerId) {
        this.askerId = askerId;
    }

    public String getPermitterId() {
        return permitterId;
    }

    public void setPermitterId(String permitterId) {
        this.permitterId = permitterId;
    }

    public FriendShip() {
    }

    public FriendShip(String askerId, String permitterId) {
        this.askerId = askerId;
        this.permitterId = permitterId;
    }
}
