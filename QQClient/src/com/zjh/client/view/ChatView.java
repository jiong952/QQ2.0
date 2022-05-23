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
import javax.swing.border.TitledBorder;
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
    JTabbedPane eastPane; //东部面板
    /**文本域**/
    JTextArea chatArea; //聊天文本域
    JTextArea sendArea; //发送文本域
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
        frame.setBounds((d.width-d.width/3)/2,(d.height-d.height/3)/2-200,650,600);
        frame.setIconImage((new ImageIcon("img/icon.jpg").getImage()));
        //隐藏聊天框
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
            }
        });
        frame.setResizable(false);
        northPanel = north();
        centerPanel = center();
        eastPane = east();
        southPanel = south();
        frame.add(northPanel,BorderLayout.NORTH);
        frame.add(centerPanel,BorderLayout.CENTER);
        frame.add(southPanel,BorderLayout.SOUTH);
        frame.add(eastPane,BorderLayout.EAST);
        //可见
        frame.setVisible(true);
    }


    /**
     * 北部面板
     *
     * @return {@link JPanel}
     */
    public JPanel north(){
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        jPanel.setPreferredSize(new Dimension(0,40));
        //好友头像
        JLabel cs=new JLabel();
        cs.setBounds(260,5,30,30);
        ImageIcon imageIcon=new ImageIcon(friend.getAvatar());
        if(!friend.isOnLine()){
            imageIcon = Utility.getGrayImage(imageIcon);
        }
        //设置缩放图片
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(cs.getWidth(),-1,Image.SCALE_DEFAULT));
        cs.setIcon(imageIcon);
        //昵称
        JLabel nameJLabel = new JLabel();
        nameJLabel.setText(friend.getFriendName());
        nameJLabel.setBounds(300,5,100,15);
        nameJLabel.setFont(new Font("黑体",Font.BOLD,10));//字体和字体大小
        JLabel remarkJLabel = new JLabel();
        remarkJLabel.setText("("+friend.getRemark()+")");
        remarkJLabel.setBounds(300,22,100,15);
        remarkJLabel.setFont(new Font("黑体",Font.BOLD,10));//字体和字体大小
        remarkJLabel.setForeground(new Color(100,149,238));
        jPanel.add(cs);
        jPanel.add(nameJLabel);
        jPanel.add(remarkJLabel);
        //备注
        return jPanel;
    }

    /**
     * 中部滚动面板
     *
     * @return {@link JPanel}
     */
    public JPanel center(){
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(450,400));
        //使用append方法追加 \n
        chatArea = new JTextArea();
        chatArea.setEnabled(false);
        chatArea.append(user.getUserName() + new Date() + "\n");
        chatArea.append("  哈哈哈你好" + "\n");
        chatArea.append(friend.getFriendName() + new Date() + "\n");
        chatArea.append("  嗯嗯" + "\n");
        chatArea.append(user.getUserName() + new Date() + "\n");
        chatArea.append("  你吃了吗" + "\n");
        JScrollPane jScrollPane = new JScrollPane(chatArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setPreferredSize(new Dimension(420,380));
        jScrollPane.setBorder(new TitledBorder("聊天窗口"));
        //封装
        panel.add(jScrollPane);
        return panel;
    }

    /**
     * 文件选项卡
     *
     * @return {@link JTabbedPane}
     */
    public JTabbedPane east(){
        JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
        jTabbedPane.setPreferredSize(new Dimension(200,0));
        jTabbedPane.setBorder(new TitledBorder("接收文件"));
        return jTabbedPane;
    }

    /**
     * 南部聊天面板
     *
     * @return {@link JPanel}
     */
    public JPanel south(){
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(450,160));
//        panel.setBorder(new TitledBorder("发送窗口"));
        sendArea = new JTextArea();
        sendArea.setLineWrap(true);
        JScrollPane jScrollPane = new JScrollPane(sendArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setBounds(5,0,420,120);
        jScrollPane.setBorder(new TitledBorder("发送窗口"));
        chatHisButton = new JButton("聊天记录");
        chatHisButton.setBounds(30,125,100,30);
        sendButton = new JButton("发送");
        sendButton.setBounds(330,125,80,30);
        panel.add(jScrollPane);
        panel.add(chatHisButton);
        panel.add(sendButton);
        return panel;
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

    public Container getContainer(){
        //封装
        Container container=new Container();
        container.setSize(400,30);
        container.setLayout(null);
        //头像
        JLabel jLabel=new JLabel();
        jLabel.setSize(30,30);
        ImageIcon imageIcon = new ImageIcon(user.getAvatar());
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(jLabel.getWidth(),-1,Image.SCALE_DEFAULT));
        jLabel.setIcon(imageIcon);
        jLabel.setLocation(0,0);
        container.add(jLabel);
        //名字
        JLabel jLabel1=new JLabel(user.getUserName() + "  " + new Date() ,SwingConstants.LEFT);
        jLabel1.setSize(350,15);
        jLabel1.setFont(new Font("黑体",Font.BOLD,10));
        jLabel1.setLocation(40,0);
        container.add(jLabel1);
        //消息内容
        JLabel jLabel2=new JLabel("这是一条消息哈哈哈哈哈",SwingConstants.LEFT);
        jLabel2.setSize(350,15);
        jLabel2.setFont(new Font("楷体",Font.BOLD,10));
        jLabel2.setLocation(40,30);
        container.add(jLabel2);
        container.setVisible(true);
        return container;
    }
}
