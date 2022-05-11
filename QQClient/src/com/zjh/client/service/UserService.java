package com.zjh.client.service;

import com.zjh.client.service.thread.ClientConnectServerThread;
import com.zjh.client.service.thread.ManageClientConnectServerThread;
import com.zjh.common.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//参考mvc分层，这里做用户客户端的业务逻辑处理

/**
 * 用户客户端服务客户端登录、退出、拉取在线用户业务逻辑@
 * author 张俊鸿
 * @date 2022/05/08
 **/
public class UserService {

    private User u = new User();
    private Socket socket;

    /**
     * 检查用户
     *
     * @param userId   用户id
     * @param password 密码
     * @return {@link String}
     */
    //登录验证
    //修改逻辑为传函数名到后台checkUser调用后台dao
    public String checkUser(String userId, String password){
        u.setUserId(userId);
        u.setPassword(password);
        //和服务器端取得联系，得到Socket
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9998);
            System.out.println("socket已连接...");
            //发送序列化用户对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            RequestMsg requestMsg = new RequestMsg();
            //方法名和参数
            requestMsg.setContent("checkUser");
            requestMsg.setParams(new Object[]{u});
            oos.writeObject(requestMsg);
//            System.out.println(requestMsg);
            //接收服务端响应的消息
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ResponseMsg responseMsg = (ResponseMsg) ois.readObject();
//            System.out.println(responseMsg);
            if(StateCode.SUCCEED.equals(responseMsg.getStateCode())){
                //登录成功
                /*创建一个和服务端保持通信的线程*/
                ClientConnectServerThread connectServerThread = new ClientConnectServerThread(socket);
                //启动线程
                connectServerThread.start();
                connectServerThread.setPriority(10);
                //为了方便管理多用户，这里创建一个类进行统一管理
                ManageClientConnectServerThread.addThread(userId,connectServerThread);
                return StateCode.SUCCEED;
            }else if(StateCode.FAIL.equals(responseMsg.getStateCode())){
                //账号名或密码错误,关闭socket
                socket.close();
                return StateCode.FAIL;
            }else if(StateCode.HAS_LOGIN.equals(responseMsg.getStateCode())){
                //已登录,关闭socket
                socket.close();
                return StateCode.HAS_LOGIN;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return StateCode.FAIL;
    }

    /**
     * 拉取在线用户
     */
    //拉取在线用户
    public void onLineFriendList(){
        //发送类型为MESSAGE_GET_ONLINE_FRIEND的Message
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSenderId(u.getUserId());
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出
     */
    public void exit(){
        //给服务端发送MESSAGE_CLIENT_EXIT类型的message
        Message message = new Message();
        message.setSenderId(u.getUserId());
        message.setMsgType(MessageType.MESSAGE_CLIENT_EXIT);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向服务端请求搜索用户
     *
     * @param userId 用户id
     * @return {@link List}<{@link User}>
     */
    public List<User> searchUserById(String myId,String userId){
        List<User> list = new ArrayList<>();
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9998);
            //发送序列化用户对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            RequestMsg requestMsg = new RequestMsg();
            //方法名和参数
            requestMsg.setRequesterId(myId);
            requestMsg.setContent("searchUserById");
            requestMsg.setParams(new Object[]{userId});
            oos.writeObject(requestMsg);
            //接收服务端响应的消息
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ResponseMsg responseMsg = (ResponseMsg) ois.readObject();
            list = (List<User>) responseMsg.getReturnValue();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}
