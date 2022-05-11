package com.zjh.server.service.thread;

import com.zjh.common.*;
import com.zjh.server.dao.UserDao;
import com.zjh.server.service.FriendService;
import com.zjh.server.service.MangeOffMsgService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这是服务器与某个用户的通讯线程
 * 在这里处理的是双向消息，即要处理返回值的消息，由用户主动发送方法请求，服务端写入，用户主动读取返回值
 * 例如登录请求，返回好友列表，注册，删除好友，等
 * @author 张俊鸿
 * @date 2022/05/08
 **/
public class ConnectToSingleThread {
    private FriendService friendService = new FriendService();
    private ServerSocket serverSocket = null;
    private UserDao userDao = new UserDao();
    //创建一个集合模拟用户数据库 ,key是userId,value是user
    //使用ConcurrentHashMap可以处理并发问题，线程安全
    private static ConcurrentHashMap<String,User> userHashMap = new ConcurrentHashMap<>();
    static {
        //静态代码块 初始化 validUsers
        userHashMap.put("admin",new User("admin","a"));
        userHashMap.put("张俊鸿",new User("张俊鸿","a"));
        userHashMap.put("com.zjh.server.utils.zjh",new User("com.zjh.server.utils.zjh","a"));
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
        User check = userDao.check(userId);
        if(check != null && userId.equals(check.getUserId()) && password.equals(check.getPassword())) return true;
        return false;
    }

    /**
     * 服务器通讯监听
     */
    public ConnectToSingleThread() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                System.out.println("用户端"+socket);
                //获取客户端发来的user
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //接收请求
                RequestMsg requestMsg = (RequestMsg) ois.readObject();
                //输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                switch (requestMsg.getContent()){
                    case "checkUser":
                        //登录验证
                        User user = (User) requestMsg.getParams()[0];
                        boolean flag = checkUser(user.getUserId(), user.getPassword());
                        if(flag){
                            //登录成功
                            //检查是否已登录
                            if(ManageServerConnectClientThread.getThread(user.getUserId()) == null){

                                String time = sdf.format(new Date());
                                System.out.println("【"+time+"】"+user.getUserId()+ "登录成功" );
                                //响应成功
                                ResponseMsg responseMsg = new ResponseMsg();
                                responseMsg.setStateCode(StateCode.SUCCEED);
                                oos.writeObject(responseMsg);
                                //创建一个线程与登录客户端保持通信
                                ServerThread serverThread = new ServerThread(user.getUserId(), socket);
                                //开启线程
                                serverThread.start();
                                //把登录线程放进集合统一管理
                                ManageServerConnectClientThread.addThread(user.getUserId(), serverThread);
                                //增加一个notifyFriend方法，通知其他好友上线状态【发一个msg到监听线程，getterId是其他用户，senderId是上线用户】
                                //client收到消息后，在调用方法，更新好友列表字段
                                //查看是否有离线消息，有就发送给他
                                friendService.notifyOther(user.getUserId());
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
                                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String time = sdf.format(new Date());
                                //登录过了
                                System.out.println("【"+time+"】"+user.getUserId()+ "已登录过");
                                //响应已登录
                                ResponseMsg responseMsg = new ResponseMsg();
                                responseMsg.setStateCode(StateCode.HAS_LOGIN);
                                oos.writeObject(responseMsg);
                            }
                        }else {
                            //登录失败
                            //账号名或密码错误
                            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String time = sdf.format(new Date());
                            System.out.println("【"+time+"】"+user.getUserId()+ "登录失败" );
                            //响应失败
                            ResponseMsg responseMsg = new ResponseMsg();
                            responseMsg.setStateCode(StateCode.FAIL);
                            oos.writeObject(responseMsg);
                            socket.close();
                        }
                        break;
                    case "findAllFriend":
                        //请求好友列表
                        String time = sdf.format(new Date());
                        System.out.println("【"+time+"】用户"+requestMsg.getRequesterId() + "请求好友列表");
                        //调用方法,将列表返回
                        List<Friend> list = friendService.findAllFriend((String) requestMsg.getParams()[0]);
                        //响应
                        ResponseMsg responseMsg = new ResponseMsg();
                        responseMsg.setReturnValue(list);
                        oos.writeObject(responseMsg);
                        break;
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
