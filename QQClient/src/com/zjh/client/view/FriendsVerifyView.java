package com.zjh.client.view;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.zjh.client.request.FriendRequest;
import com.zjh.common.User;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author 张俊鸿
 * @description: 好友验证界面
 * @since 2022-05-11 22:16
 */
@SuppressWarnings("all")
public class FriendsVerifyView extends JFrame {
    private FriendRequest friendRequest = new FriendRequest();
    JFrame frame;
    JTabbedPane jTabbedPane;
    String userId;
    public JFrame getFrame() {
        return frame;
    }
    public FriendsVerifyView(String userId){
        this.userId = userId;
        //设置窗口大小和位置
        frame = new JFrame("修改好友信息");
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension d = t.getScreenSize();
        frame.setBounds((d.width - d.width / 3) / 2, (d.height - d.height / 3) / 2 , 250, 350);
        frame.setIconImage((new ImageIcon("img/icon.jpg").getImage()));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jTabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
        jTabbedPane.setSize(200,170);
        jTabbedPane.setBorder(new TitledBorder("文件区域"));
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(200,0));
        JLabel jLabel = new JLabel("暂无好友申请");
        jLabel.setFont(new Font("黑体", Font.BOLD, 25));//字体和字体大小
        panel.add(jLabel);
        jTabbedPane.insertTab("好友申请",null,panel,"接收文件",0);
        frame.add(jTabbedPane);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new FriendsVerifyView("1");
    }

    public void addVerifyRecord(String askerId, String myId){
        // TODO: 2022-05-11 将记录加到界面中
        System.out.println("\n=========好友验证界面=========");
        System.out.println("【"+askerId+"】请求添加您为好友");
        // TODO: 2022-05-11 由于输入阻塞问题，这里默认同意，到时候用button来调用方法传参
        System.out.println("同意");
        friendRequest.permitMakeFriend(myId,askerId);
    }

    /**
     * 新的好友申请
     */
    public void receiveFri(User user){
        int tabCount = jTabbedPane.getTabCount();
        JPanel panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(250,350));
        panel1.setLayout(new FormLayout("fill:d:noGrow,left:18dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:118px:noGrow,left:4dlu:noGrow,fill:30px:noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:14px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:noGrow,top:40dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        final JLabel label1 = new JLabel();
        label1.setText("");
        CellConstraints cc = new CellConstraints();
        panel1.add(label1, cc.xy(1, 7));
        final JLabel label2 = new JLabel();
        label2.setText("");
        panel1.add(label2, cc.xy(1, 9));
        final JLabel label3 = new JLabel();
        label3.setText("ID");
        panel1.add(label3, cc.xy(3, 11, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JLabel label4 = new JLabel();
        label4.setText("昵称");
        panel1.add(label4, cc.xy(3, 13, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JLabel label5 = new JLabel();
        label5.setText(user.getUserId());
        panel1.add(label5, cc.xy(5, 11));
        final JLabel label6 = new JLabel();
        label6.setText(user.getUserName());
        panel1.add(label6, cc.xy(5, 13));
        final JLabel label7 = new JLabel();
        label7.setText("性别");
        panel1.add(label7, cc.xy(3, 15, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JLabel label8 = new JLabel();
        String gender = "";
        if(user.getGender() == 2){
            gender = "无";
        }else if(user.getGender() == 1){
            gender = "男";
        }else {
            gender = "女";
        }
        label8.setText(gender);
        panel1.add(label8, cc.xy(5, 15));
        final JLabel label9 = new JLabel();
        label9.setText("年龄");
        panel1.add(label9, cc.xy(3, 17, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JLabel label10 = new JLabel();
        label10.setText(String.valueOf(user.getAge()));
        panel1.add(label10, cc.xy(5, 17));
        final JLabel label11 = new JLabel();
        label11.setText("个性签名");
        panel1.add(label11, cc.xy(3, 19, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JLabel label12 = new JLabel();
        label12.setText(user.getSignature());
        panel1.add(label12, cc.xy(5, 19));
        final JLabel label13 = new JLabel();
        panel1.add(label13, cc.xywh(3, 3, 3, 7, CellConstraints.CENTER, CellConstraints.DEFAULT));
        ImageIcon imageIcon = new ImageIcon(user.getAvatar());
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(80, -1, Image.SCALE_DEFAULT));
        label13.setIcon(imageIcon);
        final JLabel label14 = new JLabel();
        label14.setText("");
        panel1.add(label14, cc.xy(1, 5));
        final JLabel label15 = new JLabel();
        label15.setText("");
        panel1.add(label15, cc.xy(1, 3));
        final JLabel label16 = new JLabel();
        label16.setText("");
        panel1.add(label16, cc.xy(5, 1));
        JButton perButton = new JButton();
        perButton.setText("同意");
        panel1.add(perButton, cc.xy(3, 21, CellConstraints.CENTER, CellConstraints.DEFAULT));
        perButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                friendRequest.permitMakeFriend(userId,user.getUserId());
                jTabbedPane.remove(panel1);
            }
        });
        JButton refuseButton = new JButton();
        refuseButton.setText("拒绝");
        panel1.add(refuseButton, cc.xy(5, 21));
        refuseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTabbedPane.remove(panel1);
            }
        });
        final JLabel label17 = new JLabel();
        label17.setText("");
        panel1.add(label17, cc.xy(7, 8));
        jTabbedPane.insertTab("好友"+(tabCount),null,panel1,"好友"+(tabCount),tabCount);
        jTabbedPane.setSelectedIndex(tabCount);
    }
}
