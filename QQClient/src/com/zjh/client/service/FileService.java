package com.zjh.client.service;

import com.zjh.client.manage.ManageClientConnectServerThread;
import com.zjh.common.Message;
import com.zjh.common.MessageType;
import com.zjh.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 张俊鸿
 * @description: 客户端文件业务处理逻辑类
 * @since 2022-05-08 15:07
 */
public class FileService {

    /**
     * 发送文件
     *
     * @param fromPath 文件路径
     * @param sender   发送方
     * @param getter   接收方
     */
    public void sendFile(String fromPath, String sender, String getter){
        File file = new File(fromPath);
        //根据文件路径读取本地文件到fileBytes
        byte[] fileBytes = FileUtils.readFile(fromPath);
        //封装fileMsg
        String fileName = file.getName();
        //封装Message
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_FILE);
        message.setSenderId(sender);
        message.setGetterId(getter);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = sdf.format(date);
        message.setSendTime(date);
        System.out.println("【"+time+"】 你对" + getter + "发送了：" + fileName);
        message.setFileName(fileName);
        message.setFileBytes(fileBytes);
        //获取socket发送msg
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
