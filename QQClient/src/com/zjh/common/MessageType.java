package com.zjh.common;

/**
 * 消息类型
 *
 * @author 张俊鸿
 * @date 2022/05/08
 */
public interface MessageType {
//    String MESSAGE_SUCCEED ="1"; //登录成功
//    String MESSAGE_LOGIN_FAIL ="2"; //登录失败
//    String MESSAGE_ALREADY_LOGIN ="3"; //已登录
    String MESSAGE_COMMON_MSG = "4"; //普通信息包
    String MESSAGE_GET_ONLINE_FRIEND = "5"; //客户端请求拉取在线用户列表
    String MESSAGE_RETURN_ONLINE_FRIEND = "6"; //服务端返回在线用户列表
    String MESSAGE_CLIENT_EXIT = "7"; //客户端请求退出
    String MESSAGE_TO_ALL_MSG = "8"; //群发信息包
    String MESSAGE_GROUP_CHAT = "9"; //群聊信息包
    String MESSAGE_FILE = "10"; //文件信息包
    String MESSAGE_NEWS = "11"; //服务端推送消息
    String NEW_ONLINE = "12"; //有新的好友上线了
    String ASK_MAKE_FRIEND = "13"; //好友请求
    String SUCCESS_MAKE_FRIEND_TO_ASK = "14"; //给发送好友申请的人成功提醒
    String SUCCESS_MAKE_FRIEND_TO_PERMIT = "15"; //给同意好友申请的人成功提醒
    String SEND_SUCCESS = "16";//消息发送成功
    String NEW_OFFLINE = "17"; //有好友下线了
    String SEND_SUCCESS_TO_ALL = "18";//群发成功
}
