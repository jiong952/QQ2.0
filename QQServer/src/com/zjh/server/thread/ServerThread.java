package com.zjh.server.thread;

import com.zjh.common.Message;
import com.zjh.common.MessageType;
import com.zjh.server.dao.FriendDao;
import com.zjh.server.dao.MessageDao;
import com.zjh.server.manage.ManageServerConnectClientThread;
import com.zjh.server.service.FriendService;
import com.zjh.server.service.MangeOffMsgService;
import com.zjh.server.service.MessageService;
import com.zjh.server.utils.FileUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 这是服务器后台，多对多，和所有在线用户都有想通的管道
 * 主要转发消息
 * @author 张俊鸿
 * @date 2022/05/08
 **/
public class ServerThread extends Thread{
    private MessageService messageService = new MessageService();
    private FriendService friendService = new FriendService();
    //和客户端通讯的socket
    private Socket socket;
    private String userId;

    public ServerThread(String userId, Socket socket) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    //接受或发送消息
    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (true){
            try {
                System.out.println("服务端通讯线程等待客户端发送的消息......");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果服务端没发送消息，线程会阻塞在这里
                Message msg = (Message)ois.readObject();
                if(MessageType.MESSAGE_GET_ONLINE_FRIEND.equals(msg.getMsgType())){
                    //客户端请求拉取在线用户列表
                    System.out.println(msg.getSenderId() + "请求在线用户列表");
                    //调用方法获得用户列表
                    String onlineUserList = ManageServerConnectClientThread.returnOnlineUserList();
                    System.out.println(onlineUserList);
                    //封装返回
                    Message msg_back = new Message();
                    msg_back.setContent(onlineUserList);
                    msg_back.setGetterId(msg.getSenderId());
                    msg_back.setMsgType(MessageType.MESSAGE_RETURN_ONLINE_FRIEND);
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(msg_back);
                }else if(MessageType.MESSAGE_CLIENT_EXIT.equals(msg.getMsgType())){
                    //客户端安全退出
                    //通知好友下线了
                    friendService.notifyOther(msg.getSenderId(),MessageType.NEW_OFFLINE);
                    //在集合中去除客户端
                    ManageServerConnectClientThread.removeThread(msg.getSenderId());
                    String time = sdf.format(new Date());
                    System.out.println("【"+time+"】用户"+msg.getSenderId() + "退出");
                    System.out.println("当前在线人数："+ManageServerConnectClientThread.onLineNum() + "人");
                    socket.close();
                    break;
                }else if(MessageType.MESSAGE_COMMON_MSG.equals(msg.getMsgType())){
                    //私聊功能
                    System.out.println("【"+msg.getSendTime()+"】"+msg.getSenderId() + " 向" + msg.getGetterId() + "发送了: "+msg.getContent());
                    //查看是否在线
                    ServerThread thread = ManageServerConnectClientThread.getThread(msg.getGetterId());
                    ServerThread thread2 = ManageServerConnectClientThread.getThread(msg.getSenderId());
                    Boolean flag = false;
                    if(thread != null){
                        //在线直接发送
                        ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                        oos.writeObject(msg);
                        flag = messageService.insertMsg(msg,true);
                    }else {
                        //离线存入暂存
                        flag =  messageService.insertMsg(msg,false);
//                        MangeOffMsgService.addOffMsg(msg.getGetterId(),msg);
//                        System.out.println(MangeOffMsgService.getOffMsgMap());
                    }
                    if(flag){
                        //消息发送成功，返回给用户
                        ObjectOutputStream oos = new ObjectOutputStream(thread2.getSocket().getOutputStream());
                        msg.setMsgType(MessageType.SEND_SUCCESS);
                        oos.writeObject(msg);
                    }
                }else if(MessageType.MESSAGE_TO_ALL_MSG.equals(msg.getMsgType())){
                    //群发功能 给除了自己之外的所有人
                    System.out.println("【"+msg.getSendTime()+"】"+msg.getSenderId() + " 向所有人发送了: "+msg.getContent());
                    //服务端承担消息转发的作用
                    //获取在线列表，去除自己
                    List<String> userList = ConnectToSingleControllerThread.getAllUser();
//                    String onlineUserList = ManageServerConnectClientThread.returnOnlineUserList();
//                    String[] users = onlineUserList.split(" ");
                    for (int i = 0; i < userList.size(); i++) {
                        //校验，排除自己
                        if(!userList.get(i).equals(msg.getSenderId())){
                            //查看是否在线
                            ServerThread thread = ManageServerConnectClientThread.getThread(userList.get(i));
                            if(thread != null){
                                //在线直接发送
                                ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                                oos.writeObject(msg);
                            }else {
                                //不在线，要留言
                                //离线存入暂存
                                MangeOffMsgService.addOffMsg(userList.get(i),msg);
//                                System.out.println(MangeOffMsgService.getOffMsgMap());
                            }
                        }
                    }

                }else if(MessageType.MESSAGE_GROUP_CHAT.equals(msg.getMsgType())){
                    //群聊功能
                    System.out.println("【"+msg.getSendTime()+"】"+msg.getSenderId() +  " 向" + msg.getGetterId() + "发送了: " +msg.getContent());
                    //服务端承担消息转发的作用
                    String[] getters = msg.getGetterId().split(" ");
                    for (int i = 0; i < getters.length; i++) {
                        //查看是否在线
                        ServerThread thread = ManageServerConnectClientThread.getThread(getters[i]);
                        if(thread != null){
                            //在线直接发送
                            ObjectOutputStream oos = new ObjectOutputStream(ManageServerConnectClientThread.getThread(getters[i]).getSocket().getOutputStream());
                            oos.writeObject(msg);
                        }else {
                            //不在线，要留言
                            //离线存入暂存
                            MangeOffMsgService.addOffMsg(getters[i],msg);
                        }

                    }
                }else if(MessageType.MESSAGE_FILE.equals(msg.getMsgType())){
                    //发送文件功能
                    System.out.println("【"+msg.getSendTime()+"】"+msg.getSenderId() +  " 向" + msg.getGetterId() + "发送了: " +msg.getFileName());
                    //转发消息
                    //判断是否在线
                    ServerThread thread = ManageServerConnectClientThread.getThread(msg.getGetterId());
                    ServerThread thread2 = ManageServerConnectClientThread.getThread(msg.getSenderId());
                    //保存文件到服务端本地
                    String desc = "C:\\Users\\Mono\\Desktop\\用户文件暂存\\" + msg.getGetterId() +"_" + new Date().getTime() + "_" + msg.getFileName();
                    FileUtils.storeFile(msg.getFileBytes(),desc);
                    //文件内容改为路径
                    Boolean flag = false;
                    msg.setContent(desc);
                    if(thread != null){
                        ObjectOutputStream oos = new ObjectOutputStream(ManageServerConnectClientThread.getThread(msg.getGetterId()).getSocket().getOutputStream());
                        oos.writeObject(msg);
                        flag = messageService.insertMsg(msg,true);
                    }else {
                        //不在线，要留言
                        flag = messageService.insertMsg(msg,false);
                    }
                    if(flag){
                        //消息发送成功，返回给用户
                        ObjectOutputStream oos = new ObjectOutputStream(thread2.getSocket().getOutputStream());
                        msg.setContent(msg.getFileName());
                        msg.setMsgType(MessageType.SEND_SUCCESS);
                        oos.writeObject(msg);
                    }
                }else {
                    System.out.println("其他类型消息，暂不处理");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
