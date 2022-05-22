package com.zjh.client.view;

import java.awt.*;//导包
import javax.swing.*;
public class Swing_JTabbedPane extends JFrame{//继承JFrame顶层容器

    //定义组件
    JTabbedPane jtbp; //定义选项卡
    JPanel jp1,jp2,jp3;	//定义面板
    public static void main(String[] args) {
        Swing_JTabbedPane a=new Swing_JTabbedPane();//

    }
    public Swing_JTabbedPane()//构造函数
    {
        JTabbedPane a = new MyQQView().center();
        //创建组件
//        jtbp=new JTabbedPane();	//创建选项卡
//        jtbp.add("选项一",jp1);	//创建三个面板
//        jtbp.add("选项二",jp2);
//        jtbp.add("选项三",jp3);

        //设置布局管理器

        //添加组件
        this.add(a);//添加选项卡窗格到容器

        //设置界面属性
        //设置窗体实行
        this.setTitle("选项卡案例");		//设置界面标题
        this.setSize(350, 300);				//设置界面像素
        this.setLocation(200, 200);			//设置界面初始位置
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//设置虚拟机和界面一同关闭
        this.setVisible(true);				//设置界面可视化
    }
}