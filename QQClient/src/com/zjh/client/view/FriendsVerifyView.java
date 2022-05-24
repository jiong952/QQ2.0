package com.zjh.client.view;

import com.zjh.client.request.FriendRequest;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

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

    public FriendsVerifyView(String userId){
        this.userId = userId;
        //设置窗口大小和位置
        frame = new JFrame("修改好友信息");
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension d = t.getScreenSize();
        frame.setBounds((d.width - d.width / 3) / 2, (d.height - d.height / 3) / 2 - 200, 100, 150);
        frame.setIconImage((new ImageIcon("img/icon.jpg").getImage()));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jTabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
        jTabbedPane.setSize(200,170);
        jTabbedPane.setBorder(new TitledBorder("文件区域"));

        frame.setVisible(true);
    }

    public void addVerifyRecord(String askerId, String myId){
        // TODO: 2022-05-11 将记录加到界面中
        System.out.println("\n=========好友验证界面=========");
        System.out.println("【"+askerId+"】请求添加您为好友");
        // TODO: 2022-05-11 由于输入阻塞问题，这里默认同意，到时候用button来调用方法传参
        System.out.println("同意");
        friendRequest.permitMakeFriend(myId,askerId);
    }
}
