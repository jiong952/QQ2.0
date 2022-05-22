package com.zjh.client.view;

import com.zjh.client.manage.ManageUser;
import com.zjh.client.request.FriendRequest;
import com.zjh.client.service.MessageService;
import com.zjh.common.Friend;
import com.zjh.common.Message;
import com.zjh.common.User;
import com.zjh.utils.FileUtils;
import com.zjh.utils.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.List;

/**
 * @author 张俊鸿
 * @description: 私聊聊天界面
 * @since 2022-05-12 1:38
 */
public class ChatView extends JFrame {
    private MessageService messageService = new MessageService();
    private FriendRequest friendRequest = new FriendRequest();
    private User user;
    private Friend friend;
    /**窗口**/
    JFrame frame; //frame窗口
    /**面板**/
    JPanel northPanel;//北部面板
    JPanel centerPanel;//中部面板
    JPanel southPanel;//南部面板
    /**按钮**/
    JButton sendButton;//发送按钮
    JButton chatHisButton;//聊天记录按钮按钮

    public JFrame getFrame() {
        return frame;
    }

    public ChatView(String userId, String friendId) {
        //初始化页面
        user = ManageUser.getUser(userId);
        List<Friend> allFriend = friendRequest.findAllFriend(userId);
        for (Friend friend1 : allFriend) {
            if(friend1.getFriendId().equals(friendId)){
                friend = friend1;
                break;
            }
        }
        //设置窗口大小和位置
        frame = new JFrame("与 "+friend.getFriendName()+" ("+friend.getRemark()+") 聊天中......");
        Toolkit t=Toolkit.getDefaultToolkit();
        Dimension d=t.getScreenSize();
        frame.setBounds((d.width-d.width/3)/2,(d.height-d.height/3)/2,650,600);
        frame.setIconImage((new ImageIcon("img/icon.jpg").getImage()));
        //隐藏聊天框
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
            }
        });
//        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setResizable(false);
        JPanel panel = new JPanel();
        //可见
        frame.setVisible(true);
    }

    public void chat(){
//        // TODO: 2022-05-12 这个方法之后删掉，改为button的触发事件
//        System.out.println("\n========="+userId+"(我)与"+friendId+"的私聊界面=========");
//        System.out.print("请输入发送的内容：");
//        String chatContent = Utility.readString(100); //聊天内容
//        //先验证一下是不是好友
//        boolean b = friendRequest.checkFriend(userId, friendId);
//        if(!b){
//            //被单删了
//            // TODO: 2022-05-12 在这个页面写一个弹窗 先设置为不可见
//            System.out.println("对方不是你的好友，请先添加好友");
//        }else {
//            //调用一个MessageClientService的发送消息
//            messageService.privateChat(chatContent,userId,friendId);
//        }
    }

    /**
     * 发送消息或者收到消息，刷新页面
     *
     * @param message 消息
     */
    public void addMessage(Message message){
        // TODO: 2022-05-12 把消息加进去，重新刷新页面
//        System.out.println("【"+message.getSendTime()+"】" + (message.getSenderId().equals(userId) ? "你" : message.getSenderId()) + "发送了：" +message.getContent());
    }

    /**
     * 收到文件信息
     *
     * @param msg 消息
     */
    public void getFile(Message msg){
        // TODO: 2022-05-12 右侧框显示发来的文件信息，提供按钮下载和拒收
        //拿到文件信息
        System.out.println("【"+msg.getSendTime()+"】"+msg.getSenderId()+"对你发送了：" +msg.getFileName());
        // TODO: 2022-05-12 用户选择接收会弹出一个文件路径框，点击后将文件保存到对应路径
        String desc = "D:\\" + msg.getGetterId() +"_" + new Date().getTime() + "_" + msg.getFileName();
        acceptFile(desc,msg.getFileBytes(),msg.getFileName());
    }

    /**
     * 这到时候是点击文件选择器的确定按钮后出发的动作
     *
     * @param desc      desc
     * @param fileBytes 文件字节数
     * @param fileName  文件名称
     */
    public void acceptFile(String desc,byte[] fileBytes,String fileName){
        FileUtils.storeFile(fileBytes,desc);
        System.out.println(fileName+ " 已保存到" + desc);
    }
}
