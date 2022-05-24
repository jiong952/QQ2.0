package com.zjh.client.view;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.zjh.client.manage.ManageChatView;
import com.zjh.client.request.FriendRequest;
import com.zjh.common.Friend;
import com.zjh.common.MessageType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * @author 张俊鸿
 * @description:
 * @since 2022-05-24 14:11
 */
public class updateFriView extends JFrame {
    private JPanel panel;
    private JTextField remarkField;
    private JCheckBox isStarBox;
    private JButton updateButton;
    private JButton cancelButton;
    private Friend friend;
    private String userId;
    JFrame frame;

    public updateFriView(Friend friend,String userId) {
        panel = new JPanel();
        panel.setLayout(new FormLayout("fill:86px:noGrow,left:52dlu:noGrow,left:4dlu:noGrow,fill:51px:noGrow,fill:82px:grow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:33px:noGrow,top:4dlu:noGrow,center:31px:noGrow,top:4dlu:noGrow,center:31px:noGrow,top:4dlu:noGrow,center:31px:noGrow,top:4dlu:noGrow,center:31px:noGrow,top:4dlu:noGrow,center:31px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        final JLabel label1 = new JLabel();
        Font label1Font = UIManager.getFont("Label.font");
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-16777216));
        label1.setHorizontalAlignment(0);
        label1.setText("好友昵称：");
        CellConstraints cc = new CellConstraints();
        panel.add(label1, cc.xy(1, 5, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label2 = new JLabel();
        Font label2Font = UIManager.getFont("Label.font");
        if (label2Font != null) label2.setFont(label2Font);
        label2.setForeground(new Color(-16777216));
        label2.setHorizontalAlignment(0);
        label2.setText("好友性别：");
        panel.add(label2, cc.xy(1, 7, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label3 = new JLabel();
        Font label3Font = UIManager.getFont("Label.font");
        if (label3Font != null) label3.setFont(label3Font);
        label3.setForeground(new Color(-16777216));
        label3.setHorizontalAlignment(0);
        label3.setText("好友年龄：");
        panel.add(label3, cc.xy(1, 9, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label4 = new JLabel();
        Font label4Font = UIManager.getFont("Label.font");
        if (label4Font != null) label4.setFont(label4Font);
        label4.setForeground(new Color(-16777216));
        label4.setHorizontalAlignment(0);
        label4.setText("个性签名：");
        panel.add(label4, cc.xy(1, 11));
        final JLabel label5 = new JLabel();
        Font label5Font = UIManager.getFont("Label.font");
        if (label5Font != null) label5.setFont(label5Font);
        label5.setForeground(new Color(-16777216));
        label5.setHorizontalAlignment(0);
        label5.setText("好友ID：");
        panel.add(label5, cc.xy(1, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel label6 = new JLabel();
        label6.setHorizontalAlignment(0);
        label6.setText(friend.getFriendId());
        panel.add(label6, cc.xy(2, 3, CellConstraints.LEFT, CellConstraints.DEFAULT));
        final JLabel label7 = new JLabel();
        label7.setHorizontalAlignment(0);
        label7.setText(friend.getFriendName());
        panel.add(label7, cc.xy(2, 5, CellConstraints.LEFT, CellConstraints.DEFAULT));
        final JLabel label8 = new JLabel();
        label8.setHorizontalAlignment(0);
        String gender = "";
        if(friend.getGender() == 2){
            gender = "好友不愿意透露";
        }else if(friend.getGender() == 1){
            gender = "男";
        }else {
            gender = "女";
        }
        label8.setText(gender);
        panel.add(label8, cc.xy(2, 7, CellConstraints.LEFT, CellConstraints.DEFAULT));
        final JLabel label9 = new JLabel();
        label9.setHorizontalAlignment(0);
        label9.setText(String.valueOf(friend.getAge()));
        panel.add(label9, cc.xy(2, 9, CellConstraints.LEFT, CellConstraints.DEFAULT));
        final JLabel label10 = new JLabel();
        label10.setHorizontalAlignment(0);
        label10.setText(friend.getSignature());
        panel.add(label10, cc.xy(2, 11, CellConstraints.LEFT, CellConstraints.DEFAULT));
        final JLabel label11 = new JLabel();
        label11.setText("");
        panel.add(label11, cc.xy(2, 1));
        final JLabel label12 = new JLabel();
        label12.setSize(60,60);
        ImageIcon imageIcon=new ImageIcon(friend.getAvatar());
        //设置缩放图片
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(label12.getWidth(),-1,Image.SCALE_DEFAULT));
        label12.setIcon(imageIcon);
        panel.add(label12, cc.xywh(4, 3, 2, 5, CellConstraints.CENTER, CellConstraints.FILL));
        final JLabel label13 = new JLabel();
        label13.setText("");
        panel.add(label13, cc.xyw(1, 17, 5));
        final JLabel label14 = new JLabel();
        Font label14Font = UIManager.getFont("Label.font");
        if (label14Font != null) label14.setFont(label14Font);
        label14.setForeground(new Color(-16777216));
        label14.setText("备注");
        panel.add(label14, cc.xy(4, 9, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JLabel label15 = new JLabel();
        Font label15Font = UIManager.getFont("Label.font");
        if (label15Font != null) label15.setFont(label15Font);
        label15.setForeground(new Color(-16777216));
        label15.setText("星标");
        panel.add(label15, cc.xy(4, 11, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JLabel label16 = new JLabel();
        if(friend.isAsk()){
            label16.setText("你于"+friend.getTime()+"向他发送好友申请");
        }else {
            label16.setText("你于"+friend.getTime()+"同意好友申请");
        }
        panel.add(label16, cc.xyw(2, 13, 3, CellConstraints.LEFT, CellConstraints.DEFAULT));
        final JLabel label17 = new JLabel();
        Font label17Font = UIManager.getFont("Label.font");
        if (label17Font != null) label17.setFont(label17Font);
        label17.setForeground(new Color(-16777216));
        label17.setText("成为好友：");
        panel.add(label17, cc.xy(1, 13, CellConstraints.CENTER, CellConstraints.DEFAULT));
        remarkField = new JTextField();
        remarkField.setText(friend.getRemark());
        panel.add(remarkField, cc.xy(5, 9, CellConstraints.LEFT, CellConstraints.DEFAULT));
        isStarBox = new JCheckBox();
        isStarBox.setText("星标好友");
        panel.add(isStarBox, cc.xy(5, 11, CellConstraints.LEFT, CellConstraints.DEFAULT));
        updateButton = new JButton();
        Font updateButtonFont = UIManager.getFont("Button.font");
        if (updateButtonFont != null) updateButton.setFont(updateButtonFont);
        updateButton.setText("修改");
        panel.add(updateButton, cc.xyw(3, 15, 2));
        cancelButton = new JButton();
        Font cancelButtonFont = UIManager.getFont("Button.font");
        if (cancelButtonFont != null) cancelButton.setFont(cancelButtonFont);
        cancelButton.setHorizontalTextPosition(0);
        cancelButton.setText("取消");
        panel.add(cancelButton, cc.xy(5, 15, CellConstraints.CENTER, CellConstraints.DEFAULT));
        if(friend.isStar()){
            isStarBox.setSelected(true);
        }else {
            isStarBox.setSelected(false);
        }
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int check = JOptionPane.showConfirmDialog(frame, "确定修改？", "确认提示", 0);
                if(check == 0){
                    //修改信息
                    new FriendRequest().updateFriend(userId,friend.getFriendId(),remarkField.getText(),isStarBox.isSelected());
                    //更新好友列表
                    List<Friend> allFriend = new FriendRequest().findAllFriend(userId);
                    MyQQView.refreshFriendList(allFriend);
                    //修改聊天框备注
                    ChatView view = ManageChatView.getView(friend.getFriendId());
                    if(view != null) {
                        //窗口存在
                        view.changeRemark(remarkField.getText());
                    }
                    //成功弹窗
                    JOptionPane.showMessageDialog(null,"修改成功！");
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });


        this.friend = friend;
        //设置窗口大小和位置
        frame = new JFrame("修改好友信息");
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension d = t.getScreenSize();
        frame.setBounds((d.width - d.width / 3) / 2, (d.height - d.height / 3) / 2 - 200, 200, 250);
        frame.setIconImage((new ImageIcon("img/icon.jpg").getImage()));
        frame.setResizable(false);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

//    public static void main(String[] args) {
//        new updateFriView(new Friend());
//    }
//
//    {

}
