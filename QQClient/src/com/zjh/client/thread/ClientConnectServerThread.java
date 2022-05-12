package com.zjh.client.thread;

import com.zjh.client.manage.ManageChatView;
import com.zjh.client.request.FriendRequest;
import com.zjh.client.view.ChatView;
import com.zjh.client.view.FriendListView;
import com.zjh.client.view.FriendsVerifyView;
import com.zjh.client.view.NotificationView;
import com.zjh.common.Friend;
import com.zjh.common.Message;
import com.zjh.common.MessageType;
import com.zjh.utils.FileUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;

/**
 * 这里是客户端与服务端的通讯线程，主要读取或发送消息
 * 即别的客户端发来的消息，文件等
 * @author 张俊鸿
 * @date 2022/05/08
 **/


public class ClientConnectServerThread extends Thread{
    private static Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    //接受或发送消息
    @Override
    public void run() {
        while (true){
            try {
                System.out.println("客户端通讯线程等待服务端发送的消息......");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果服务端没发送消息，线程会阻塞在这里
                Message msg = (Message)ois.readObject();
                //服务端返回用户列表
                if(MessageType.MESSAGE_RETURN_ONLINE_FRIEND.equals(msg.getMsgType())){
                    //取出用户列表进行展示,真正项目其实是用json前后端交互，这里用String简便处理
                    String[] onlineUsers = msg.getContent().split(" ");
                    System.out.println("========当前在线用户列表========");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("用户：" + onlineUsers[i]);
                    }
                }else if(MessageType.MESSAGE_COMMON_MSG.equals(msg.getMsgType()) || MessageType.MESSAGE_TO_ALL_MSG.equals(msg.getMsgType())){
                    //私聊和群发的本质是一样的
                    //查看界面是否存在
                    ChatView view = ManageChatView.getView(msg.getGetterId());
                    if(view != null){
                        //窗口存在
                        if(view.isVisible()){
                            //可见，那就是被最小化了
                            if(view.getExtendedState() == 1){
                                //最小化
                                // TODO: 2022-05-12 看看能不能做个最小化闪烁
                            }
                        }
                    }else {
                        //窗口不存在
                        // TODO: 2022-05-12 提示音 弹窗
                        view = new ChatView(msg.getGetterId(), msg.getSenderId());
                        // TODO: 2022-05-12 设置为不可见
                        System.out.println("\n========="+msg.getGetterId()+"(我)与"+msg.getSenderId()+"的私聊界面=========");
                    }
                    view.addMessage(msg);
                }else if(MessageType.MESSAGE_GROUP_CHAT.equals(msg.getMsgType())){
                    //群聊功能
                    System.out.println("\n========="+msg.getSenderId() +" "+msg.getGetterId()+"的群聊界面=========");
                    System.out.println("【"+msg.getSendTime()+"】"+msg.getSenderId()+"发送了：" +msg.getContent());
                }else if(MessageType.MESSAGE_FILE.equals(msg.getMsgType())){
                    //发送文件过来
                    System.out.println("\n=========与"+msg.getSenderId()+"的私聊界面=========");
                    //拿到文件信息
                    System.out.println("【"+msg.getSendTime()+"】"+msg.getSenderId()+"对你发送了：" +msg.getFileName());
                    //用户选择存放路径 这里无法选择路径，因为子线程会和主线程同时阻塞等待scanner，发生冲突
                    //但后期使用图形化界面就不会同时争夺同一个资源（文件管理器）
                    //先用封装替代
                    String desc = "D:\\" + msg.getGetterId() +"_" + new Date().getTime() + "_" + msg.getFileName();
                    FileUtils.storeFile(msg.getFileBytes(),desc);
                    System.out.println(msg.getFileName()+ " 已保存到" + desc);
                }else if(MessageType.MESSAGE_NEWS.equals(msg.getMsgType())){
                    //服务端推送的消息
                    System.out.println("\n=========服务端推送界面=========");
                    System.out.println("【"+msg.getSendTime()+"】"+msg.getSenderId()+"对你发送了：" +msg.getContent());
                }else if(MessageType.NEW_ONLINE.equals(msg.getMsgType())){
                    //新好友上线
                    new NotificationView().onLineRemind(msg.getSenderId());
                    //刷新好友列表
                    List<Friend> allFriend = new FriendRequest().findAllFriend(msg.getGetterId());
                    new FriendListView().showFriendList(allFriend);
                }else if(MessageType.ASK_MAKE_FRIEND.equals(msg.getMsgType())){
                    //好友申请
                    new FriendsVerifyView().addVerifyRecord(msg.getSenderId(),msg.getGetterId());
                }else if(MessageType.SUCCESS_MAKE_FRIEND_TO_ASK.equals(msg.getMsgType())){
                    //申请好友成功
                    new NotificationView().askMakeFriendSuccess(msg.getSenderId());
                    //刷新好友列表
                    List<Friend> allFriend = new FriendRequest().findAllFriend(msg.getGetterId());
                    new FriendListView().showFriendList(allFriend);
                }else if(MessageType.SUCCESS_MAKE_FRIEND_TO_PERMIT.equals(msg.getMsgType())){
                    //同意好友申请成功
                    new NotificationView().permitMakeFriendSuccess(msg.getSenderId());
                    //刷新好友列表
                    List<Friend> allFriend = new FriendRequest().findAllFriend(msg.getGetterId());
                    new FriendListView().showFriendList(allFriend);
                }else if(MessageType.SEND_SUCCESS.equals(msg.getMsgType())){
                    //发送消息成功
                    new ChatView(msg.getSenderId(),msg.getGetterId()).addMessage(msg);
                }else {
                    System.out.println("其他类型msg，暂时不处理");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
