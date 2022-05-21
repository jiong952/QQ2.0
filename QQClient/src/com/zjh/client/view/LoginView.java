package com.zjh.client.view;

import com.zjh.client.request.UserRequest;
import com.zjh.common.StateCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author 张俊鸿
 * @description: 登录界面
 * @since 2022-05-21 22:08
 */
public class LoginView extends JFrame {
    private UserRequest userRequest = new UserRequest();

    /**窗口**/
    JFrame frame; //frame窗口
    /**面板**/
    JPanel northPanel;//北部面板
    JPanel westPanel;//西部面板
    JPanel centerPanel;//中部面板
    JPanel eastPanel;//东部面板
    JPanel southPanel;//南部面板
    /**按钮**/
    JButton loginButton;//登录按钮
    /**标签**/
    JLabel registerLabel;//东部注册账号标签
    JLabel forgetPwdLabel;//东部忘记密码标签
    /**输入框**/
    // TODO: 2022-05-21 使用焦点进行提示输入
    JTextField idField;//中部账号文本框
    JPasswordField pwdField;//中部密码框
    /**复选框**/
    JCheckBox autoLoginBox;//中部自动登录复选框
    JCheckBox rememberPwdBox;//中部记住密码复选框
    /**状态记录**/
    static boolean isAutoLogin;//记住密码复选框的当前状态
    static boolean isRememberPwd;//自动登录复选框的当前状态
    /**登录按钮监听对象**/
    loginButtonHandler loginButtonHandler;



    public static void main(String[] args) {
        new LoginView();
    }

    public LoginView(){
        //设置窗口大小和位置
        frame = new JFrame("登录界面");
        Toolkit t=Toolkit.getDefaultToolkit();
        Dimension d=t.getScreenSize();
        frame.setBounds((d.width-d.width/3)/2,(d.height-d.height/3)/2,350,250);
        frame.setIconImage((new ImageIcon("img/icon.jpg").getImage()));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);


        //加入各部分面板
        northPanel = north();
        westPanel = west();
        centerPanel = center();
        southPanel = south();
        eastPanel = east();
        frame.add(northPanel,BorderLayout.NORTH);
        frame.add(westPanel,BorderLayout.WEST);
        frame.add(southPanel,BorderLayout.SOUTH);
        frame.add(centerPanel,BorderLayout.CENTER);
        frame.add(eastPanel,BorderLayout.EAST);

        //可见
        frame.setVisible(true);

    }
    //创建北部面板
    public JPanel north(){
        JPanel jp1=new JPanel();
        jp1.setLayout(null);
        jp1.setPreferredSize(new Dimension(0,90));
        ImageIcon in=new ImageIcon((new ImageIcon("img/login_bg.jpeg").getImage()));
        JLabel cc=new JLabel(in);
        cc.setBounds(0,0,500,90);
        cc.setOpaque(false);
        jp1.add(cc);
        return jp1;
    }
    //创建西部面板
    public JPanel west(){
        JPanel jp2=new JPanel();
        jp2.setLayout(null);
        jp2.setPreferredSize(new Dimension(90,0));
        ImageIcon ss=new ImageIcon((new ImageIcon("img/icon.jpg").getImage()));
        JLabel cs=new JLabel(ss);
        cs.setBounds(5,10,75,75);
        jp2.add(cs);
        return jp2;
    }
    //创建中部面板
    public JPanel center(){
        JPanel jp4=new JPanel();
        jp4.setLayout(null);
        jp4.setPreferredSize(new Dimension(0,180));
        idField=new JTextField(10);//最多存放10个字
        idField.setBounds(0,10,160,25);
        idField.setFont(new Font("黑体",Font.BOLD,13));//字体和字体大小
        rememberPwdBox=new JCheckBox("记住密码",isRememberPwd);
        rememberPwdBox.setBounds(0,60,80,18);
        rememberPwdBox.setFont(new Font("黑体",Font.BOLD,10));
        autoLoginBox=new JCheckBox("自动登录",isAutoLogin);
        autoLoginBox.setBounds(80, 60, 80, 18);
        autoLoginBox.setFont(new Font("黑体",Font.BOLD,10));
        idField.addFocusListener(new JTextFieldHdandler(idField,rememberPwdBox,"QQ号"));
        idField.setOpaque(false);
        jp4.add(idField);
        pwdField=new JPasswordField(18);
        pwdField.setBounds(0,35, 160,25);
        pwdField.setFont(new Font("黑体",Font.BOLD,13));
        pwdField.addFocusListener(new JPasswordFielddHdandler(pwdField,rememberPwdBox,"密码"));
        pwdField.setOpaque(false);
        jp4.add(rememberPwdBox);
        jp4.add(pwdField);
        jp4.add(autoLoginBox);
        loginButtonHandler = new loginButtonHandler(idField,pwdField);
        return jp4;
    }
    //创建东部面板
    public JPanel east(){
        JPanel jp5=new JPanel();
        jp5.setLayout(null);
        jp5.setPreferredSize(new Dimension(80,0));
        registerLabel=new JLabel("注册账号");
        registerLabel.setBounds(0, 10, 70, 25);
        registerLabel.setFont(new Font("黑体",Font.BOLD,13));
        registerLabel.setForeground(new Color(100,149,238));
        // TODO: 2022-05-22 注册功能
//        registerLabel.addMouseListener(new LabelHandler(enrollLabel));
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//将鼠标设置为手型
        forgetPwdLabel=new JLabel("忘记密码");
        forgetPwdLabel.setBounds(0, 35, 70, 25);
        forgetPwdLabel.setFont(new  Font("黑体",Font.BOLD,13));
        forgetPwdLabel.setForeground(new Color(100,149,238));
        // TODO: 2022-05-22 忘记密码功能
//        forgetPwdLabel.addMouseListener(new LabelHandler(codeLabel));
        forgetPwdLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jp5.add(registerLabel);
        jp5.add(forgetPwdLabel);
        return jp5;
    }
    //创建南部面板
    public JPanel south(){
        JPanel jp3=new JPanel();
        jp3.setLayout(null);
        jp3.setPreferredSize(new Dimension(0,40));
        loginButton= new JButton(new ImageIcon((new ImageIcon("img/login.png").getImage())));
        loginButton.setBounds(85,0,160,25);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//将鼠标设置为手掌型
        loginButton.setContentAreaFilled(false);
        loginButton.setFocusPainted(false);
        jp3.add(loginButton);
        loginButton.addActionListener(loginButtonHandler);
        return jp3;
    }


    //实现文本框的焦点功能，当焦点不在文本框内时，显示默认提示信息（QQ号）
    public class JTextFieldHdandler implements FocusListener {
        private String str;
        private JTextField text1;
        private JCheckBox rememberMima;
        public JTextFieldHdandler(JTextField text1,JCheckBox rememberMima,String str) {
            this.text1=text1;
            this.rememberMima=rememberMima;
            this.str=str;
            if(rememberMima.isSelected())
            {
                File file=new File("d:\\保存记住的账号.txt");
                try {
                    InputStream in=new FileInputStream(file);
                    byte[] bn=new byte[(int)file.length()];
                    in.read(bn);
                    in.close();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                text1.setForeground(Color.BLACK);

            }
            else
            {
                text1.setText(str);
                text1.setForeground(Color.gray);
            }
        }
        public void focusGained(FocusEvent e) {
            if(text1.getText().equals(str))
            {
                text1.setText("");
                text1.setForeground(Color.BLACK);
            }
        }
        public void focusLost(FocusEvent e) {

            if(text1.getText().equals("")) {
                text1.setForeground(Color.gray);
                text1.setText(str);
            }
        }
    }
    //实现密码框的焦点功能，当焦点不在密码框内时，显示默认提示信息（密码）
    public class JPasswordFielddHdandler implements FocusListener{
        private String str;
        private JPasswordField text1;
        private JCheckBox rememberMima;
        public JPasswordFielddHdandler(JPasswordField text1,JCheckBox rememberMima,String str) {
            this.text1=text1;
            this.rememberMima=rememberMima;
            this.str=str;
            if(rememberMima.isSelected())
            {
                String strMima="000";
                String selectSql1="select * from userinfo where qq=?";


                text1.setText(strMima);
                text1.setEchoChar('*');
                text1.setForeground(Color.BLACK);
            }
            else
            {
                text1.setText(str);
                text1.setEchoChar((char)(0));//不设置回显
                text1.setForeground(Color.gray);
            }
        }
        public void focusGained(FocusEvent e) {
            if(text1.getText().equals(str))
            {
                text1.setText("");
                text1.setEchoChar('*');//将回显设置为'*'
                text1.setForeground(Color.BLACK);
            }
        }

        public void focusLost(FocusEvent e) {
            if(text1.getText().equals("")) {
                text1.setEchoChar((char)(0));
                text1.setForeground(Color.gray);
                text1.setText(str);
            }
        }
    }

    /**
     * 登录按钮处理程序
     *
     * @author 张俊鸿
     * @date 2022/05/22
     */
    public class loginButtonHandler implements ActionListener{
        private JTextField usernameField;
        private JPasswordField passwordField;

        public loginButtonHandler(JTextField usernameField, JPasswordField passwordField) {
            this.usernameField = usernameField;
            this.passwordField = passwordField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if("".equals(usernameField.getText())||"".equals(new String(passwordField.getPassword()))){
                JOptionPane.showMessageDialog(null,"请填写完所有信息！","错误",JOptionPane.ERROR_MESSAGE);
            }else {
                String stateCode = userRequest.checkUser(usernameField.getText(), new String(passwordField.getPassword()));
                if(StateCode.SUCCEED.equals(stateCode)){
                    JOptionPane.showMessageDialog(null,"登录成功！");
                    JOptionPane.showMessageDialog(null,"您好！用户"+usernameField.getText());
                    // TODO: 2022-05-22 跳转页面 携带数据
                }else if(StateCode.HAS_LOGIN.equals(stateCode)){
                    JOptionPane.showMessageDialog(null,"您已经登录，不可重复登录","错误",JOptionPane.ERROR_MESSAGE);
                }else {
                    JOptionPane.showMessageDialog(null,"登录失败!","错误",JOptionPane.ERROR_MESSAGE);
                }
            }

        }
    }

}
