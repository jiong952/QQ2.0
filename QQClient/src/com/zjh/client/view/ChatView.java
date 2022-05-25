package com.zjh.client.view;

import com.zjh.client.manage.ManageUser;
import com.zjh.client.request.FriendRequest;
import com.zjh.client.service.FileService;
import com.zjh.client.service.MessageService;
import com.zjh.common.Friend;
import com.zjh.common.Message;
import com.zjh.common.User;
import com.zjh.utils.FileUtils;
import com.zjh.client.thread.ProgressBarThread;
import com.zjh.utils.Utility;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
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
    private FileService fileService = new FileService();
    User user;
    Friend friend;
    /**
     * 窗口
     **/
    JFrame frame; //frame窗口
    /**
     * 面板
     **/
    JPanel northPanel;//北部面板
    JPanel centerPanel;//中部面板
    JPanel southPanel;//南部面板
    JTabbedPane eastPane; //东部面板
    /**
     * 文本域
     **/
    JTextArea chatArea; //聊天文本域
    JTextArea sendArea; //发送文本域
    /**
     * 按钮
     **/
    JButton sendButton;//发送按钮
    JButton sendFileButton;//发送文件按钮
    JButton chatHisButton;//聊天记录按钮按钮
    JButton acceptButton; //接收文件按钮
    JButton refuseButton; //拒绝接收文件按钮
    JButton changeButton; //改变路径按钮
    /**
     * 选项卡
     **/
    JTabbedPane jTabbedPane;
    /**
     * 文件选择器
     **/
    JFileChooser fileChooser;
    /**标签**/
    JLabel srcLabel; //另存路径
    JLabel srcLabel_real; //另存路径
    JLabel avatar;// 用户头像
    JLabel remarkJLabel; //备注

    public JFrame getFrame() {
        return frame;
    }

    public ChatView(String userId, String friendId) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorderPainted(true);
        //聊天记录菜单项
        JMenu chatHis = new JMenu("聊天记录");
        menuBar.add(chatHis);
        JMenuItem backUp = new JMenuItem("备份记录到本地");
        backUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageService.backUpChatHis(user.getUserId(),friend.getFriendId());
            }
        });
        chatHis.add(backUp);
        JMenuItem delete = new JMenuItem("删除聊天记录");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int check = JOptionPane.showConfirmDialog(frame, "删除则无法找回！确认删除吗", "删除提示", 0);
                if(check == 0){
                    //通知服务器 同时把所有manage中的界面删除掉
                    messageService.delChatHis(user.getUserId(),friend.getFriendId());
                    chatArea.setText("");
                    JOptionPane.showMessageDialog(null,"删除成功！");
                }
            }
        });
        //好友管理菜单项
        JMenu friendUpdate = new JMenu("好友管理");
        JMenuItem updateFriend = new JMenuItem("修改好友信息");
        updateFriend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 弹出新界面 显示好友信息 同时提供备注文本框和星标单选框
                new updateFriView(friend,user.getUserId());
            }
        });
        JMenuItem deleteFriend = new JMenuItem("删除好友");
        deleteFriend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int check = JOptionPane.showConfirmDialog(frame, "删除好友会删除好友一切信息，请确认是否删除", "删除提示", 0);
                if(check == 0){
                    //通知服务器  单向删除好友
                    friendRequest.deleteFriend(user.getUserId(),friend.getFriendId());
                    //关闭聊天框 刷新好友列表
                    frame.dispose();
                    //更新好友列表
                    List<Friend> allFriend = new FriendRequest().findAllFriend(user.getUserId());
                    MyQQView.refreshFriendList(allFriend);
                    JOptionPane.showMessageDialog(null,"删除成功！");
                }
            }
        });
        friendUpdate.add(updateFriend);
        friendUpdate.add(deleteFriend);
        chatHis.add(delete);
        menuBar.add(friendUpdate);
        //初始化文件选择器
        initFileChooser();
        //初始化页面
        user = ManageUser.getUser(userId);
        List<Friend> allFriend = friendRequest.findAllFriend(userId);
        for (Friend friend1 : allFriend) {
            if (friend1.getFriendId().equals(friendId)) {
                friend = friend1;
                break;
            }
        }
        //设置窗口大小和位置
        frame = new JFrame("与 " + friend.getFriendName() + " (" + friend.getRemark() + ") 聊天中......");
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension d = t.getScreenSize();
        frame.setBounds((d.width - d.width / 3) / 2, (d.height - d.height / 3) / 2 - 200, 650, 600);
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
        frame.add(northPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(southPanel, BorderLayout.SOUTH);
        frame.add(eastPane, BorderLayout.EAST);
        frame.setJMenuBar(menuBar);
        //可见
        frame.setVisible(true);
    }


    /**
     * 北部面板
     *
     * @return {@link JPanel}
     */
    public JPanel north() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        jPanel.setPreferredSize(new Dimension(0, 40));
        //好友头像
        avatar = new JLabel();
        avatar.setBounds(260, 5, 30, 30);
        ImageIcon imageIcon = new ImageIcon(friend.getAvatar());
        if (!friend.isOnLine()) {
            imageIcon = Utility.getGrayImage(imageIcon);
        }
        //设置缩放图片
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(avatar.getWidth(), -1, Image.SCALE_DEFAULT));
        avatar.setIcon(imageIcon);
        //昵称
        JLabel nameJLabel = new JLabel();
        nameJLabel.setText(friend.getFriendName());
        nameJLabel.setBounds(300, 5, 100, 15);
        nameJLabel.setFont(new Font("黑体", Font.BOLD, 10));//字体和字体大小
        remarkJLabel = new JLabel();
        remarkJLabel.setText("(" + friend.getRemark() + ")");
        remarkJLabel.setBounds(300, 22, 100, 15);
        remarkJLabel.setFont(new Font("黑体", Font.BOLD, 10));//字体和字体大小
        remarkJLabel.setForeground(new Color(100, 149, 238));
        jPanel.add(avatar);
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
    public JPanel center() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(450, 400));
        //使用append方法追加 \n
        chatArea = new JTextArea();
        chatArea.setEnabled(false);
        chatArea.setDisabledTextColor(Color.black);
        JScrollPane jScrollPane = new JScrollPane(chatArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setPreferredSize(new Dimension(420, 370));
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
    public JTabbedPane east() {
        jTabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
        jTabbedPane.setSize(200,170);
        jTabbedPane.setBorder(new TitledBorder("文件区域"));
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(200,0));
        JLabel jLabel = new JLabel("等待文件中...");
        jLabel.setFont(new Font("黑体", Font.BOLD, 25));//字体和字体大小
        panel.add(jLabel);
        jTabbedPane.insertTab("接收文件",null,panel,"接收文件",0);
        return jTabbedPane;
    }

    /**
     * 南部聊天面板
     *
     * @return {@link JPanel}
     */
    public JPanel south() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(450, 160));
        sendArea = new JTextArea();
        sendArea.setLineWrap(true);
        JScrollPane jScrollPane = new JScrollPane(sendArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setBounds(5, 0, 420, 120);
        jScrollPane.setBorder(new TitledBorder("发送窗口"));
        chatHisButton = new JButton("聊天记录");
        chatHisButton.setBounds(30, 125, 90, 30);
        chatHisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chatArea.setText("");
                // 清空聊天框 把所有聊天记录加进去
                List<Message> list = new MessageService().getAllMsg(user.getUserId(), friend.getFriendId());
                for (Message message : list) {
                    if(message.getSenderId().equals(user.getUserId())){
                        //自己发的
                        if(message.getFileName() != null){
                            sendSuccess(message,true);
                        }else {
                            sendSuccess(message,false);
                        }
                    }else {
                        if(message.getFileName() != null){
                            chatArea.append(friend.getFriendName() + "(" + friend.getRemark() + ")  " + message.getSendTime() + "\n");
                            chatArea.append(" " + message.getFileName() + "\n");
                        }else {
                            receiveMsg(message);
                        }
                    }
                }
            }
        });
        sendFileButton = new JButton("发送文件");
        sendFileButton.setBounds(150, 125, 90, 30);
        sendFileButton.addActionListener(new sendFileButtonHandler());
        sendButton = new JButton("发送消息");
        sendButton.setBounds(330, 125, 90, 30);
        //加入按钮事件
        sendButton.addActionListener(new sendButtonHandler());
        panel.add(jScrollPane);
        panel.add(chatHisButton);
        panel.add(sendButton);
        panel.add(sendFileButton);
        return panel;
    }


    public void initFileChooser() {
        //读取桌面路径
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File homeDirectory = fileSystemView.getHomeDirectory();
        fileChooser = new JFileChooser();
        //设置当前目录
        fileChooser.setCurrentDirectory(homeDirectory);
        fileChooser.setVisible(false);
    }

    public class sendButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String sendContent = sendArea.getText();
            if (sendContent.equals("")) {
                JOptionPane.showMessageDialog(null, "消息不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                //先验证一下是不是好友
                boolean b = friendRequest.checkFriend(user.getUserId(), friend.getFriendId());
                if (!b) {
                    // 被单删了
                    // 2022-05-12 在这个页面写一个弹窗 先设置为不可见
                    JOptionPane.showMessageDialog(null, "对方不是您的好友！请先添加", "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    //调用一个MessageClientService的发送消息
                    messageService.privateChat(sendContent, user.getUserId(), friend.getFriendId());
                    //清空
                    sendArea.setText("");
                }
            }
        }
    }


    public class sendFileButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            fileChooser.setVisible(true);
            int result = fileChooser.showDialog(null, "发送");
            if (result == JFileChooser.APPROVE_OPTION) {
                //发送文件
                File selectedFile = fileChooser.getSelectedFile();
                fileService.sendFile(selectedFile.getAbsolutePath(), user.getUserId(), friend.getFriendId());
                fileChooser.setVisible(false);
            } else {
                //其他
                JOptionPane.showMessageDialog(null, "已取消发送");
                fileChooser.setVisible(false);
            }
        }
    }


    /**
     * 发送消息成功，把消息加到聊天框
     *
     * @param message 消息
     */
    public void sendSuccess(Message message, boolean isFile) {
        chatArea.append(user.getUserName() + "(我)  " + message.getSendTime() + "\n");
        if (isFile) {
            chatArea.append(" 【" + message.getFileName() + "】发送成功！\n");
        } else {
            chatArea.append(" " + message.getContent() + "\n");
        }
    }

    /**
     * 收到消息
     *
     * @param message 消息
     */
    public void receiveMsg(Message message) {
        chatArea.append(friend.getFriendName() + "(" + friend.getRemark() + ")  " + message.getSendTime() + "\n");
        chatArea.append(" " + message.getContent() + "\n");
    }

    /**
     * 接收文件
     */
    public void receiveFile(Message message) {
        int tabCount = jTabbedPane.getTabCount();
        JPanel panel = new JPanel();
        JPanel panel_north = new JPanel(new BorderLayout());
        panel_north.setPreferredSize(new Dimension(200,150));
        panel.setLayout(new BorderLayout());
        panel.setSize(200,150);
        //文件类型图 如果是图片则直接显示发送的图片？
        JLabel picLabel = new JLabel();
//        picLabel.setBounds(0, 0, 70, 70);
        picLabel.setSize(70,70);
        JPanel in_panel = new JPanel(new GridLayout(4, 1));
        String s = message.getFileName().split("\\.")[1];
        if(s.equals("jpg") || s.equals("png") || s.equals("doc") || s.equals("pdf") || s.equals("avi")){
            s = "img/"+s+".jpg";
        }else {
            s = "img/file.png";
        }
        ImageIcon imageIcon = new ImageIcon(s);
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(picLabel.getWidth(), -1, Image.SCALE_DEFAULT));
        picLabel.setIcon(imageIcon);
        panel_north.add(picLabel,BorderLayout.WEST);

        //文件名和文件大小
        JLabel nameLabel = new JLabel(" 文件名：" + message.getFileName());
        picLabel.setBounds(70, 2, 70, 30);
        nameLabel.setFont(new Font("黑体", Font.BOLD, 13));
        JLabel sizeLabel = new JLabel(" 文件大小：" + message.getFileBytes().length / 1024 + "KB");
        sizeLabel.setBounds(70, 40, 70, 30);
        sizeLabel.setFont(new Font("黑体", Font.BOLD, 13));
        JLabel empty= new JLabel();
        JLabel empty2= new JLabel();
        in_panel.add(empty);
        in_panel.add(nameLabel);
        in_panel.add(sizeLabel);
        in_panel.add(empty2);
        panel_north.add(in_panel,BorderLayout.CENTER);

        //进度条
        JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setBounds(3, 80, 182, 15);
        panel_north.add(progressBar,BorderLayout.SOUTH);
        JPanel panel_center = new JPanel();
        panel_center.setPreferredSize(new Dimension(200,100));
        //另存路径
        srcLabel = new JLabel();
        srcLabel.setBounds(0, 0, 180,100);
        JlabelSetText(srcLabel,"另存为:"+"D:\\" + message.getGetterId() + "_" + new Date().getTime() + "_" + message.getFileName());
        srcLabel_real = new JLabel();
        srcLabel_real.setText("D:\\" + message.getGetterId() + "_" + new Date().getTime() + "_" + message.getFileName());
        srcLabel_real.setVisible(false);
        panel_center.add(srcLabel);
        JPanel panel_south = new JPanel(new GridLayout(1, 3));

        //按钮
        //以另存路径接收文件 触发进度条
        acceptButton = new JButton("接收");
        acceptButton.setBounds(0,140,50,25);
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProgressBarThread(progressBar,jTabbedPane,panel,srcLabel_real.getText(),message.getFileBytes(),chatArea,message,friend).start();

            }
        });
        panel_south.add(acceptButton);
        //拒绝接收
        refuseButton = new JButton("拒绝");
        refuseButton.setBounds(60,140,50,25);
        refuseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTabbedPane.remove(panel);
            }
        });
        panel_south.add(refuseButton);
        //改变路径
        changeButton = new JButton("另存");
        changeButton.setBounds(120,140,50,25);
        changeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //读取桌面路径
                FileSystemView fileSystemView = FileSystemView.getFileSystemView();
                File homeDirectory = fileSystemView.getHomeDirectory();
                JFileChooser fileChooser2 = new JFileChooser();
                //设置当前目录
                fileChooser2.setCurrentDirectory(homeDirectory);
                fileChooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser2.setVisible(true);
                int result = fileChooser2.showDialog(null, "确定");
                if (result == JFileChooser.APPROVE_OPTION) {
                    String absolutePath = fileChooser2.getSelectedFile().getAbsolutePath() + "\\" + message.getFileName();
                    JlabelSetText(srcLabel,"另存为："+absolutePath);
                    srcLabel_real.setText(absolutePath);
                }
            }
        });
        panel_south.add(changeButton);
        panel.add(panel_north,BorderLayout.NORTH);
        panel.add(panel_center,BorderLayout.CENTER);
        panel.add(panel_south,BorderLayout.SOUTH);
        jTabbedPane.insertTab("文件"+(tabCount),null,panel,"文件"+(tabCount),tabCount);
        jTabbedPane.setSelectedIndex(tabCount);
    }

    public void JlabelSetText(JLabel jLabel, String longString) {
        StringBuilder builder = new StringBuilder("<html>");
        char[] chars = longString.toCharArray();
        FontMetrics fontMetrics = jLabel.getFontMetrics(jLabel.getFont());
        int start = 0;
        int len = 0;
        while (start + len < longString.length()) {
            while (true) {
                len++;
                if (start + len > longString.length()) break;
                if (fontMetrics.charsWidth(chars, start, len)
                        > jLabel.getWidth()) {
                    break;
                }
            }
            builder.append(chars, start, len - 1).append("<br/>");
            start = start + len - 1;
            len = 0;
        }
        builder.append(chars, start, longString.length() - start);
        builder.append("</html>");
        jLabel.setText(builder.toString());
    }

    /**
     * 改变头像颜色
     *
     * @param isOnline 在线
     */
    public void changeAvatar(boolean isOnline){
        // TODO: 2022-05-24 修改头像框颜色
        ImageIcon imageIcon = new ImageIcon(friend.getAvatar());
        if (!isOnline) {
            imageIcon = Utility.getGrayImage(imageIcon);
        }
        //设置缩放图片
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(avatar.getWidth(), -1, Image.SCALE_DEFAULT));
        avatar.setIcon(imageIcon);
    }

    /**
     * 改变备注
     *
     * @param remark 备注
     */
    public void changeRemark(String remark){
        remarkJLabel.setText(remark);
    }
}

