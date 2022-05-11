package com.zjh.server.service.thread;

import com.zjh.common.*;
import com.zjh.server.dao.UserDao;
import com.zjh.server.service.FriendService;
import com.zjh.server.service.MangeOffMsgService;
import com.zjh.server.service.UserService;

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
    private UserService userService = new UserService();
    private ServerSocket serverSocket = null;
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
                        boolean flag = userService.checkUser(user.getUserId(), user.getPassword());
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
                    case "searchUserById":
                        //根据id模糊查询搜索用户
                        String time2 = sdf.format(new Date());
                        System.out.println("【"+time2+"】用户"+requestMsg.getRequesterId() + "搜索用户");
                        //到db搜索
                        List<User> list1 = userService.searchUserById((String) requestMsg.getParams()[0]);
                        //响应
                        ResponseMsg responseMsg2 = new ResponseMsg();
                        responseMsg2.setReturnValue(list1);
                        oos.writeObject(responseMsg2);
                        break;
                    case "checkFriend":
                        //添加好友前查看是否已经是好友
                        String myId0 = requestMsg.getRequesterId();
                        String friendId0 = (String)requestMsg.getParams()[0];
                        String time0 = sdf.format(new Date());
                        System.out.println("【"+time0+"】用户"+myId0+"查看和"+friendId0+"是否是好友");
                        //到db检索
                        boolean b = friendService.checkFriend(myId0, friendId0);
                        //响应回去
                        ResponseMsg responseMsg3 = new ResponseMsg();
                        responseMsg3.setReturnValue(b);
                        oos.writeObject(responseMsg3);
                        break;
                    case "askMakeFriend":
                        //用户发送好友申请
                        String myId = requestMsg.getRequesterId();
                        String friendId = (String)requestMsg.getParams()[0];
                        String time3 = sdf.format(new Date());
                        System.out.println("【"+time3+"】用户"+myId+"向"+friendId+"发送好友申请");
                        //转发好友申请消息
                        Message msg = new Message();
                        msg.setMsgType(MessageType.ASK_MAKE_FRIEND);
                        msg.setSenderId(myId);
                        msg.setGetterId(friendId);
                        ObjectOutputStream os = new ObjectOutputStream(ManageServerConnectClientThread.getThread(friendId).getSocket().getOutputStream());
                        os.writeObject(msg);
                        break;
                    case "permitMakeFriend":
                        //用户同意好友请求
                        String myId2 = requestMsg.getRequesterId();
                        String askerId = (String)requestMsg.getParams()[0];
                        Date date = new Date();
                        String time4 = sdf.format(date);
                        System.out.println("【"+time4+"】用户"+myId2+"同意"+askerId+"的好友请求");
                        friendService.addFriend(myId2,askerId,date);
                        //通知申请人成功
                        Message askSuccessMsg = new Message();
                        askSuccessMsg.setMsgType(MessageType.SUCCESS_MAKE_FRIEND_TO_ASK);
                        askSuccessMsg.setGetterId(askerId);
                        askSuccessMsg.setSenderId(myId2);
                        ObjectOutputStream os1 = new ObjectOutputStream(ManageServerConnectClientThread.getThread(askerId).getSocket().getOutputStream());
                        os1.writeObject(askSuccessMsg);
                        //通知同意者成功
                        Message permitSuccessMsg = new Message();
                        permitSuccessMsg.setMsgType(MessageType.SUCCESS_MAKE_FRIEND_TO_PERMIT);
                        permitSuccessMsg.setGetterId(myId2);
                        permitSuccessMsg.setSenderId(askerId);
                        ObjectOutputStream os2 = new ObjectOutputStream(ManageServerConnectClientThread.getThread(myId2).getSocket().getOutputStream());
                        os2.writeObject(permitSuccessMsg);
                        break;
                    case "deleteFriend":
                        //用户单方面删除好友
                        String myId5 = requestMsg.getRequesterId();
                        String friendId5 = (String)requestMsg.getParams()[0];
                        String time5 = sdf.format(new Date());
                        System.out.println("【"+time5+"】用户"+myId5+"删除好友"+friendId5);
                        //调用方法
                        boolean b1 = friendService.deleteFriend(myId5, friendId5);
                        //响应
                        ResponseMsg responseMsg5 = new ResponseMsg();
                        responseMsg5.setReturnValue(b1);
                        oos.writeObject(responseMsg5);
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
