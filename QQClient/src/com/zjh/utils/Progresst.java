package com.zjh.utils;

import javax.swing.*;

/**
 * @author 张俊鸿
 * @description: 文件接收进度条
 * @since 2022-05-23 21:35
 */
public class Progresst extends Thread
{
    JProgressBar progressBar;
    JTabbedPane jTabbedPane;
    JPanel panel;
    String path;
    byte[] fileBytes;
    //进度条上的数字
    int[] progressValues={6,18,27,39,51,66,81,100};
    public Progresst(JProgressBar progressBar,JTabbedPane jTabbedPane,JPanel panel,String path,byte[] fileBytes)
    {
        this.progressBar=progressBar;
        this.jTabbedPane = jTabbedPane;
        this.panel = panel;
        this.path = path;
        this.fileBytes = fileBytes;
    }
    public void run()
    {
        for(int i=0;i<progressValues.length;i++)
        {
            try
            {
                Thread.sleep(500);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            //设置进度条的值
            progressBar.setValue(progressValues[i]);
        }
        progressBar.setIndeterminate(false);
        progressBar.setString("文件接收成功");
        FileUtils.storeFile(fileBytes,path);
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        jTabbedPane.remove(panel);
        JOptionPane.showMessageDialog(null,"文件已保存到"+path);
    }
}