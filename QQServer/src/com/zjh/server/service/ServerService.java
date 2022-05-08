package com.zjh.server.service;

import com.zjh.common.Message;
import com.zjh.common.MessageType;
import com.zjh.common.User;
import com.zjh.server.service.thread.ManageServerConnectClientThread;
import com.zjh.server.service.thread.SendNewsThread;
import com.zjh.server.service.thread.ServerConnectClientThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器端业务逻辑处理
 * @author 张俊鸿
 * @date 2022/05/08
 **/
public class ServerService {
    private ServerSocket serverSocket = null;
    //创建一个集合模拟用户数据库 ,key是userId,value是user
    //使用ConcurrentHashMap可以处理并发问题，线程安全
    private static ConcurrentHashMap<String,User> userHashMap = new ConcurrentHashMap<>();
    static {
        //静态代码块 初始化 validUsers
        userHashMap.put("admin",new User("admin","a"));
        userHashMap.put("张俊鸿",new User("张俊鸿","a"));
        userHashMap.put("zjh",new User("zjh","a"));
        userHashMap.put("a",new User("a","a"));
    }


    /**
     * 返回所有用户
     * @return {@link List}<{@link String}>
     */
    public static List<String> getAllUser(){
        List<String> users = new ArrayList<>();
        Iterator<String> iterator = userHashMap.keySet().iterator();
        while (iterator.hasNext()){
            users.add(iterator.next());
        }
        return users;
    }

    /**
     * 用户登录验证
     *
     * @param userId   用户id
     * @param password 密码
     * @return boolean
     */
    private boolean checkUser(String userId, String password){
        boolean b  = false;
        //用户不存在
        if(userHashMap.get(userId) == null){
            return false;
        }else if(!userHashMap.get(userId).getPassword().equals(password)){
            //密码不正确
            return false;
        }else {
            b = true;
        }
        return b;
    }

    /**
     * 服务器通讯监听
     */
    public ServerService() {
        //服务端在9999端口监听
        try {
            System.out.println("服务器端启动.....");
            System.out.println("服务端在9998端口监听");
            serverSocket = new ServerSocket(9998);
            //启动推送服务
            new SendNewsThread().start();
            //持续监听多用户
            while (true){
                Socket socket = serverSocket.accept();
                //获取客户端发来的user
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User)ois.readObject();
                //输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                //返回给客户端的消息
                Message message = new Message();

                //1.0登录先写死用户名admin 密码123456 后续联合数据库dao
                //2.0使用HashMap模拟数据库
                if(checkUser(user.getUserId(), user.getPassword())){
                    //实现单点登录
                    if(ManageServerConnectClientThread.getThread(user.getUserId()) == null){
                        System.out.println(user.getUserId()+ "登录成功" );
                        message.setMsgType(MessageType.MESSAGE_SUCCEED);
                        oos.writeObject(message);
                        //创建一个线程与登录客户端保持通信
                        ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(user.getUserId(), socket);
                        //开启线程
                        serverConnectClientThread.start();
                        //把登录线程放进集合统一管理
                        ManageServerConnectClientThread.addThread(user.getUserId(), serverConnectClientThread);
                        //查看是否有离线消息，有就发送给他
                        ArrayList<Message> messageList = MangeOffMsgService.getOffMsgMap().get(user.getUserId());
                        if(messageList != null){
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //遍历消息发送
                            for (Message msg : messageList) {
                                System.out.println(msg);
                                ObjectOutputStream os = new ObjectOutputStream(ManageServerConnectClientThread.getThread(user.getUserId()).getSocket().getOutputStream());
                                os.writeObject(msg);

                            }
                        }
                    }else {
                        //登录过了
                        System.out.println(user.getUserId()+ "已登录过");
                        message.setMsgType(MessageType.MESSAGE_ALREADY_LOGIN);
                        oos.writeObject(message);
                    }
                }else {
                    //账号名或密码错误
                    System.out.println(user.getUserId()+ "登录失败" );
                    message.setMsgType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
