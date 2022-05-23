package com.zjh.client.service;

import com.zjh.client.manage.ManageClientConnectServerThread;
import com.zjh.common.*;
import com.zjh.utils.FileUtils;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 消息客户端服务客户端聊天功能业务逻辑
 * @author 张俊鸿
 * @date 2022/05/08
 **/


public class MessageService {

    private Socket socket;
    /**
     * 群聊功能，选择指定好友发送
     *
     * @param content 内容
     * @param sender  发送方
     * @param getters 接收方
     */
    public void groupChat(String content,String sender,String getters){
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_GROUP_CHAT);
        message.setSenderId(sender);
        message.setGetterId(getters);
        message.setContent(content);
        //Sun May 08 01:11:07 CST 2022
        //时间后期转化为正常格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = sdf.format(date);
        message.setSendTime(date);
        System.out.println("【"+time+"】 你对" + getters + "发送了：" +content);
        //从用户集合中拿到当前通讯进程，发送该消息
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 群发功能 发送给自己的所有好友 【后期拓展离线留言】
     * @param chatContent 聊天内容
     * @param senderId    发件人id
     */
    public void sendMsgToAll(String chatContent,String senderId){
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_TO_ALL_MSG);
        message.setSenderId(senderId);
        message.setContent(chatContent);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = sdf.format(date);
        message.setSendTime(date);
        //从用户集合中拿到当前通讯进程，发送该消息
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 私人聊天
     *
     * @param chatContent 聊天内容
     * @param senderId    发件人id
     * @param getterId    getter id
     */
    //私聊功能
    public void privateChat(String chatContent,String senderId,String getterId){
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_COMMON_MSG);
        message.setSenderId(senderId);
        message.setGetterId(getterId);
        message.setContent(chatContent);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = sdf.format(date);
        message.setSendTime(date);
//        System.out.println("【"+time+"】 你对" + getterId + "发送了：" +chatContent);
        //从用户集合中拿到当前通讯进程，发送该消息
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getThread(senderId).getSocket().getOutputStream());
            System.out.println("私聊" + message);
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 私聊获取聊天记录 普通消息 文件消息 群发消息
     *
     * @param myId     用户id
     * @param friendId 聊天朋友id
     * @return {@link List}<{@link Message}>
     */
    public List<Message> getAllMsg(String myId,String friendId){
        List<Message> list = new ArrayList<>();
        try {
            socket = new Socket(InetAddress.getByName(StaticString.server_ip), StaticString.server_port);
            //发送序列化用户对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            RequestMsg requestMsg = new RequestMsg();
            //方法名和参数
            requestMsg.setRequesterId(myId);
            requestMsg.setContent("getAllMsg");
            requestMsg.setParams(new Object[]{friendId});
            oos.writeObject(requestMsg);
            //接收服务端响应的消息
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ResponseMsg responseMsg = (ResponseMsg) ois.readObject();
            list = (List<Message>) responseMsg.getReturnValue();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 备份与朋友的聊天记录到本地用户文件夹
     *
     * @param myId     我身份证
     * @param friendId 朋友id
     * @return boolean
     */
    public boolean backUpChatHis(String myId,String friendId){
        boolean flag = false;
        //获得聊天记录
        List<Message> allMsg = getAllMsg(myId, friendId);
        //查看文件夹是否存在，不存在就创建D:\用户聊天记录备份\a\zjh\时间戳
        String fileDirPath = "D:\\用户聊天记录备份\\"+myId+"\\"+ friendId +"\\"+  new Date().getTime();
        File dir = new File(fileDirPath);
        if(!dir.exists()){
            //文件夹不存在
            if(dir.mkdirs()){
                //创建文件夹成功
                //开始保存文件
                //文本文件，追加模式写入txt
                String txtPath = fileDirPath + "\\"+friendId+"_"+new Date().getTime()+".txt";
                //其他单独保存
                File txtFile = new File(txtPath);
                boolean newFile = false;
                if(!txtFile.exists()){
                    //如果文件不存在就创建一个
                    try {
                        newFile = txtFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                BufferedWriter bw = null;
                if(newFile){
                    //开始遍历保存
                    for (Message message : allMsg) {
                        if(MessageType.MESSAGE_FILE.equals(message.getMsgType())){
                            //文件
                            String filePath = fileDirPath + "\\" +new Date().getTime()+"_"+message.getFileName();
                            FileUtils.storeFile(message.getFileBytes(),filePath);
                        }else {
                            try {
                                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(txtPath, true)));
                                try {
                                    bw.write("【"+message.getSendTime()+"】"+(message.getSenderId().equals(myId) ? "我":message.getSenderId())+"发送了"+message.getContent());
                                    bw.newLine();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }finally {
                                try {
                                    bw.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(null,"聊天记录已备份在"+fileDirPath);
                }
            }
        }
        return flag;
    }

    /**
     * 删除聊天记录
     *
     * @param myId     用户id
     * @param friendId 朋友id
     * @return boolean
     */
    public boolean delChatHis(String myId,String friendId){
        boolean flag = false;
        //删除本地聊天记录文件夹 D:\用户聊天记录备份\a\zjh
        String fileDirPath = "D:\\用户聊天记录备份\\"+myId+"\\"+friendId;
        File dir = new File(fileDirPath);
        if(dir.exists()){
            //删除本地文件
            FileUtils.deleteAll(dir);
        }
        //请求 修改数据库的对应表的聊天记录状态
        try {
            socket = new Socket(InetAddress.getByName(StaticString.server_ip), StaticString.server_port);
            //发送序列化用户对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            RequestMsg requestMsg = new RequestMsg();
            //方法名和参数
            requestMsg.setRequesterId(myId);
            requestMsg.setContent("updateDel");
            requestMsg.setParams(new Object[]{friendId});
            oos.writeObject(requestMsg);
            //接收服务端响应的消息
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ResponseMsg responseMsg = (ResponseMsg) ois.readObject();
            flag = (boolean) responseMsg.getReturnValue();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return flag;
    }

}
