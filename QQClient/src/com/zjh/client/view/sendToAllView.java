package com.zjh.client.view;

import com.zjh.client.service.MessageService;
import com.zjh.common.Friend;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author 张俊鸿
 * @description: 群发界面
 * @since 2022-05-25 4:20
 */
public class sendToAllView {
    JFrame frame;
    JTextArea textArea;
    JButton sendButton;
    String myId;

    public sendToAllView(String myId) {
        this.myId = myId;
        //设置窗口大小和位置
        frame = new JFrame("群发");
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension d = t.getScreenSize();
        frame.setBounds((d.width - d.width / 3) / 2, (d.height - d.height / 3) / 2 , 200, 200);
        frame.setIconImage((new ImageIcon("img/icon.jpg").getImage()));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(200,200));
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        JScrollPane jScrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setBounds( 5,0,100, 100);
        jScrollPane.setBorder(new TitledBorder("发送窗口"));
        panel.add(jScrollPane);
        sendButton = new JButton("群发");
        sendButton.setBounds(110,50,70,30);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textArea.getText();
                if(text.equals("")){
                    JOptionPane.showMessageDialog(null,"消息不可为空","错误",JOptionPane.ERROR_MESSAGE);
                }else {
                    new MessageService().sendMsgToAll(text,myId);
                }
            }
        });
        panel.add(sendButton);
        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new sendToAllView("a");
    }
}
