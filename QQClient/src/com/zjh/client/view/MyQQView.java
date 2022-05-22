package com.zjh.client.view;

import com.zjh.client.manage.ManageUser;
import com.zjh.client.request.FriendRequest;
import com.zjh.client.request.UserRequest;
import com.zjh.common.Friend;
import com.zjh.common.FriendNode;
import com.zjh.common.User;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.List;

/**
 * @author 张俊鸿
 * @description: 好友列表页面
 * @since 2022-05-11 16:18
 */
public class MyQQView extends JFrame {
    private UserRequest userRequest;
    private FriendRequest friendRequest = new FriendRequest();
    private User user;
    public static JScrollPane jsp;
    //好友列表结点
    //当前用户的id
    private String userId;

    /**窗口**/
    JFrame frame; //frame窗口
    /**面板**/
    JPanel northPanel;//北部面板
    public static JTabbedPane tab; //南部选项卡
//    JPanel southPanel;//南部面板
    /**按钮**/
    JButton updateInfoButton;//修改个人信息按钮 点击弹出UpdateInfoView
    JButton addFriendButton;//添加好友按钮 点击弹出SearchFriendView

    public static void main(String[] args) {
        new MyQQView("123");
    }

    public MyQQView() {
    }

    public MyQQView(String userId){
        this.userId = userId;
        //在管理用户信息类中拿到user全部信息
        this.user = ManageUser.getUser(userId);
        this.userRequest = new UserRequest(user);
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
        frame.setResizable(false);
        //加入各部分面板
        northPanel = north();
        tab= center();
        frame.add(northPanel,BorderLayout.NORTH);
        frame.add(tab,BorderLayout.CENTER);
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
        JLabel cs=new JLabel();
        cs.setBounds(5,15,80,80);
        ImageIcon imageIcon=new ImageIcon(user.getAvatar());
        //设置缩放图片
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(cs.getWidth(),-1,Image.SCALE_DEFAULT));
        cs.setIcon(imageIcon);
        //昵称
        JLabel nameJLabel = new JLabel();
        nameJLabel.setText(user.getUserName());
        nameJLabel.setBounds(100,15,150,20);
        nameJLabel.setFont(new Font("黑体",Font.BOLD,15));//字体和字体大小
        JLabel signJLabel = new JLabel();
        signJLabel.setText(user.getSignature());
        signJLabel.setBounds(100,40,150,20);
        signJLabel.setFont(new Font("黑体",Font.BOLD,12));//字体和字体大小
        signJLabel.setForeground(new Color(100,149,238));
        //个性签名

        jPanel.add(cs);
        jPanel.add(nameJLabel);
        jPanel.add(signJLabel);

        return jPanel;
    }


    /**
     * 南部选项卡
     *
     * @return {@link JTabbedPane}
     */
    public JTabbedPane center(){
        JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP);
        tab.add("好友列表",friendPanel());
        tab.add("群聊列表",groupPanel());
        tab.setSelectedIndex(0);
        return tab;
    }

    /**
     * 朋友列表面板
     *
     * @return {@link JPanel}
     */
    public JScrollPane friendPanel(){
        List<Friend> friendList = friendRequest.findAllFriend(userId);
        JTree jTree = getJTree(friendList);
        //把好友树放到滚动面板
        jsp = new JScrollPane(jTree);
        return jsp;
    }

    /**
     * 群里面板
     *
     * @return {@link JPanel}
     */
    public JPanel groupPanel(){
        JPanel panel = new JPanel();
        JLabel filler=new JLabel("群聊列表");
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1,1));
        panel.add(filler);
        return panel;
    }


    /**
     * 刷新好友列表
     *
     * @param allFriend 新上线好友
     */
    public static void refreshFriendList(List<Friend> allFriend){
        tab.remove(0);
        JScrollPane jScrollPane = new JScrollPane(getJTree(allFriend));
//        JTree jTree = getJTree(allFriend);
        tab.insertTab("好友列表",null,jScrollPane,"好友列表",0);
//        tab.add(jScrollPane,0);
    }

    //返回JTree的数据
    public static JTree getJTree(List<Friend> allFriend){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        DefaultMutableTreeNode friend = new DefaultMutableTreeNode("我的好友");
        DefaultMutableTreeNode stranger = new DefaultMutableTreeNode("陌生人");
        DefaultMutableTreeNode blacklist = new DefaultMutableTreeNode("黑名单");
        root.add(friend);
        root.add(stranger);
        root.add(blacklist);
        for (Friend friend1 : allFriend) {
            // TODO: 2022-05-22 先不区分黑名单 后续拓展
            friend.add(new FriendNode(friend1));
        }
        DefaultTreeModel defaultTreeModel = new DefaultTreeModel(root);
        JTree contacts_tree = new JTree();
        // 设置数据
        contacts_tree.setModel(defaultTreeModel);
        // 设置为点击一次展开
        contacts_tree.setToggleClickCount(1);
        // 隐藏根节点
        contacts_tree.setRootVisible(false);
        // 展开树(在根节点隐藏时,能看见子节点)
        contacts_tree.expandPath(new TreePath(defaultTreeModel.getRoot()));
        // 隐藏根柄
        contacts_tree.setShowsRootHandles(false);
        //自定义列表样式
        contacts_tree.setCellRenderer(new DefaultTreeCellRenderer(){
            // 收起和展开图片设置为三角形
            final ImageIcon closeIcon = new ImageIcon("img/close.png");
            final ImageIcon openIcon = new ImageIcon("img/open.png");
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                          boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                //节点为展开时显示的图片
                if (!expanded) {
                    setIcon(closeIcon);
                } else {
                    setIcon(openIcon);
                }
                //设置二级列表
                String str = value.toString();
                if (!str.equals("我的好友") && !str.equals("黑名单") && !str.equals("陌生人")) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                    FriendNode people = (FriendNode) node;
                    // 根据不同的好友对象设置他们的头像
                    setIcon(new ImageIcon(people.getImageIcon().getImage().getScaledInstance(30, 30,
                            JFrame.DO_NOTHING_ON_CLOSE)));
                    // 将好友的昵称字体设置得比类别更小
                    setFont(new Font("宋体", Font.BOLD, 12));
                } else {
                    setFont(new Font("宋体", Font.BOLD, 15));
                }
                // 设置未选中节点时背景色为白色且完全透明，0表示透明,255表示正常
                setBackgroundNonSelectionColor(new Color(255, 255, 255, 0));
                // 设置选中节点时背景色为白色，透明度改为100，来区分未选中状态
                setBackgroundSelectionColor(new Color(255, 255, 255, 100));
                return this;
            }
        });
        // 使节点能响应相应操作
        contacts_tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Object node = contacts_tree.getLastSelectedPathComponent();
                String str = node.toString();
                if (!str.equals("我的好友") && !str.equals("黑名单") && !str.equals("陌生人") && e.getClickCount() == 2) {
                    //todo 点击两次好友，弹出对话框
                    JOptionPane.showMessageDialog(null,"和"+str+"聊天");
                }
            }

        });
        return contacts_tree;
    }
}
