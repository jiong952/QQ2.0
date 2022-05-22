package com.zjh.client.view;

import com.zjh.client.manage.ManageUser;
import com.zjh.client.request.UserRequest;
import com.zjh.common.Friend;
import com.zjh.common.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * @author 张俊鸿
 * @description: 好友列表页面
 * @since 2022-05-11 16:18
 */
public class MyQQView extends JFrame {
    private UserRequest userRequest;
    private User user;
    //当前用户的id
    private String userId;
    /**窗口**/
    JFrame frame; //frame窗口
    /**面板**/
    JPanel northPanel;//北部面板
    JPanel southPanel;//南部面板
    /**按钮**/
    JButton updateInfoButton;//修改个人信息按钮 点击弹出UpdateInfoView
    JButton addFriendButton;//添加好友按钮 点击弹出SearchFriendView
    /**标签**/
    JLabel avatarLabel;//北部头像标签
    JLabel userNameLabel;//北部用户名标签
    JLabel signatureLabel;//北部个性签名标签

    public static void main(String[] args) {
        new MyQQView("123");
    }

    public MyQQView(String userId){
        this.userId = userId;
        //在管理用户信息类中拿到user全部信息
        this.user = ManageUser.getUser(userId);
        this.userRequest = new UserRequest(user);
        System.out.println(user);
        //设置窗口大小和位置
        frame = new JFrame(userId);
        Toolkit t=Toolkit.getDefaultToolkit();
        Dimension d=t.getScreenSize();
        frame.setBounds(d.width-250,0,250,500);
        frame.setIconImage((new ImageIcon("img/icon.jpg").getImage()));
        //用户主窗口关闭逻辑 关闭通知服务器正常退出
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //0表示Y 1表示N
                int check = JOptionPane.showConfirmDialog(frame, "确定要退出吗？", "退出提示", 0);
                if(check == 0){
                    //通知服务器 同时把所有manage中的界面删除掉
                    userRequest.exit();
                    System.out.println("用户退出");
                }else {
                    System.out.println("用户取消退出");
                    //不做任何事情
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }

            }
        });
//        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        //加入各部分面板
        northPanel = north();
        southPanel = south();
        frame.add(northPanel,BorderLayout.NORTH);
        frame.add(southPanel,BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    /**
     * 北部用户面板
     *
     * @return {@link JPanel}
     */
    public JPanel north(){
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        jPanel.setPreferredSize(new Dimension(0,100));
        //头像
        ImageIcon ss=new ImageIcon(user.getAvatar());
        JLabel cs=new JLabel(ss);
        cs.setBounds(5,15,80,80);
        //昵称
        JLabel nameJLabel = new JLabel();
        nameJLabel.setText(user.getUserName());
        nameJLabel.setBounds(100,15,150,20);
        nameJLabel.setFont(new Font("黑体",Font.BOLD,15));//字体和字体大小
        JLabel signJLabel = new JLabel();
        signJLabel.setText(user.getSignature());
        signJLabel.setBounds(100,40,150,20);
        signJLabel.setFont(new Font("黑体",Font.BOLD,15));//字体和字体大小
        //个性签名

        jPanel.add(cs);
        jPanel.add(nameJLabel);
        jPanel.add(signJLabel);

        return jPanel;
    }

    /**
     * 南部面板(选项卡 好友 群聊 滚动面板)
     *
     * @return {@link JPanel}
     */
    public JPanel south(){
        JPanel jPanel = new JPanel();
        return jPanel;
    }


    public void showFriendList(List<Friend> allFriend){
        // TODO: 2022-05-11 后期上方会有表格类，调用方法刷新表格数据
        System.out.println("\n=========好友列表=========");
        for (Friend friend : allFriend) {
            System.out.println("【"+(friend.isOnLine() == true? "在线":"离线")+"】"+friend.getFriendId());
        }
    }
}
