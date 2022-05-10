package com.zjh.server.view;

import com.zjh.server.service.thread.ConnectToSingleThread;

/**
 * 服务器启动类
 *
 * @author 张俊鸿
 * @date 2022/05/08
 */
public class ServerFrame {
    public static void main(String[] args) {
        new ConnectToSingleThread();
    }
}
