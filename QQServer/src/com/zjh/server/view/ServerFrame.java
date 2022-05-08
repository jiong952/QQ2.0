package com.zjh.server.view;

import com.zjh.server.service.ServerService;

/**
 * 服务器启动类
 *
 * @author 张俊鸿
 * @date 2022/05/08
 */
public class ServerFrame {
    public static void main(String[] args) {
        new ServerService();
        System.out.println("服务器端启动.....");
    }
}
