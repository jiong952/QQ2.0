package com.zjh.client.view;

import com.zjh.client.request.FriendRequest;
import com.zjh.client.request.UserRequest;
import com.zjh.common.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * @author 张俊鸿
 * @description: 搜索好友界面
 * @since 2022-05-25 1:47
 */
public class SearchFriView extends JFrame {
    private UserRequest userRequest = new UserRequest();
    private FriendRequest friendRequest = new FriendRequest();
    private String myId;
    private JTextField searchFiled;
    private JButton searchButton;
    private JButton addButton;
    private JTable table;
    private JFrame frame;
    List<User> list_real = null;
    DefaultTableModel defaultTableModel;
    String[] columnNames = {"编号","ID","昵称","性别","年龄","个性签名"};

    public SearchFriView(String myId) {
        this.myId = myId;
        //设置窗口大小和位置
        frame = new JFrame("搜索好友");
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension d = t.getScreenSize();
        frame.setBounds((d.width - d.width / 3) / 2, (d.height - d.height / 3) / 2, 500, 600);
        frame.setIconImage((new ImageIcon("img/icon.jpg").getImage()));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        //北部面板
        Panel panel_north = new Panel();
        JLabel label = new JLabel("请输入搜索ID");
        panel_north.add(label);
        searchFiled = new JTextField(10);
        searchButton = new JButton("搜索");
        addButton = new JButton("添加");
        panel_north.add(label);
        panel_north.add(searchFiled);
        panel_north.add(searchButton);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchFiledText = searchFiled.getText();
                if(searchFiledText.equals("")){
                    JOptionPane.showMessageDialog(null,"搜索值不可为空","错误",JOptionPane.ERROR_MESSAGE);
                }else {
                    List<User> list = userRequest.searchUserById(myId,searchFiledText);
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getUserId().equals(myId)){
                            list.remove(i);
                        }
                    }
                    list_real = list;
                    Object[][] dataProcess = new Object[list.size()][7];
                    for (int i = 0; i < list.size(); i++) {
                        dataProcess[i][0] = i;
                        dataProcess[i][1] = list.get(i).getUserId();
                        dataProcess[i][2] = list.get(i).getUserName();
                        String gender = "";
                        if(list.get(i).getGender() == 2){
                            gender = "无";
                        }else if(list.get(i).getGender() == 1){
                            gender = "男";
                        }else {
                            gender = "女";
                        }
                        dataProcess[i][3] = gender;
                        dataProcess[i][4] = list.get(i).getAge();
                        dataProcess[i][5] = list.get(i).getSignature();
                    }
                    defaultTableModel.setDataVector(dataProcess,columnNames);
                }
            }
        });
        panel_north.add(addButton);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(table.getSelectedRow()<0){
                    JOptionPane.showMessageDialog(null,"请先选择要添加的好友!","错误",JOptionPane.ERROR_MESSAGE);
                }else {
                    String friendId = list_real.get(table.getSelectedRow()).getUserId();
                    boolean b = friendRequest.checkFriend(myId, friendId);
                    if(b){
                        JOptionPane.showMessageDialog(null,"你们已经是好友，不可重复添加!","错误",JOptionPane.ERROR_MESSAGE);
                    }else {
                        friendRequest.askMakeFriend(myId, friendId);
                        JOptionPane.showMessageDialog(null,"成功向"+ friendId +"发送好友申请");
                    }
                }
            }
        });
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBorder(BorderFactory.createTitledBorder(null, "搜索结果表", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        table = new JTable();

        Object[][] data = {};
        defaultTableModel = new DefaultTableModel(data, columnNames);
        table.setModel(defaultTableModel);
        //单选
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(60);
        table.getTableHeader().setReorderingAllowed(false);   //不可整列移动
        table.getTableHeader().setResizingAllowed(false);
        jScrollPane.setViewportView(table);
        frame.add(panel_north,BorderLayout.NORTH);
        frame.add(jScrollPane);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
//        new SearchFriView();
    }
}