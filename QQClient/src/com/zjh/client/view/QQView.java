package com.zjh.client.view;

import com.zjh.client.service.UserClientService;
import com.zjh.common.MessageType;
import com.zjh.utils.Utility;

public class QQView {
    private UserClientService userClientService = new UserClientService();
    public static void main(String[] args) {
        new QQView().showMenu();
        System.out.println("客户端退出.......");
    }

    private boolean loop = true; //控制菜单是否显示
    private String key; //控制台指令
    //显示主菜单
    private void showMenu(){
        while (loop){
            System.out.println("======欢迎登录多用户通信系统======");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.println("请输入你的选择:");
            //输入一位指令,根据指令执行不同逻辑
            key = Utility.readString(1);
            switch (key){
                case "1":
                    System.out.print("请输入用户名：");
                    String userId = Utility.readString(20);
                    System.out.print("请输入密 码：");
                    String password = Utility.readString(20);
                    //登录验证
                    String msg = userClientService.checkUser(userId, password);
                    if(MessageType.MESSAGE_SUCCEED.equals(msg)){
                        System.out.println("======欢迎用户("+userId+")======");
                        while (loop){
                            System.out.println("\n======多用户通信系统二级菜单======");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私发消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.println("请输入你的选择:");
                            //输入一位指令,根据指令执行不同逻辑
                            String command = Utility.readString(1);
                            switch (command){
                                case "1":
//                                    System.out.println("显示在线用户列表");
                                    userClientService.onLineFriendList();
                                    //主线程休眠一会，使得用户列表信息先显示
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case "2":
                                    System.out.println("群发消息");
                                    break;
                                case "3":
                                    System.out.println("私发消息");
                                    break;
                                case "4":
                                    System.out.println("发送文件");
                                    break;
                                case "9":
                                    System.out.println("退出系统");
                                    loop = false;
                                    break;
                            }
                        }
                    }else if(MessageType.MESSAGE_LOGIN_FAIL.equals(msg)){
                        System.out.println("登录失败 账号名或密码错误！");
                    }else {
                        System.out.println("您已登录,请勿重复登录!");
                    }
                    break;
                case "9":
                    System.out.println("退出系统");
                    loop = false;
                    break;
            }

        }
    }
}
