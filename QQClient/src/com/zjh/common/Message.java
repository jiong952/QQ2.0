package com.zjh.common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * 消息类
 * @author 张俊鸿
 * @date 2022/05/08
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private int msgId;
    private String senderId;
    private String getterId;
    private String groupId;
    private String content;
    private Date sendTime;
    private String msgType;
    private byte[] fileBytes;
    private String fileName;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getGetterId() {
        return getterId;
    }

    public void setGetterId(String getterId) {
        this.getterId = getterId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        //格式化输入一下，方便打印
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(this.sendTime);
        return time;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msgId=" + msgId +
                ", senderId='" + senderId + '\'' +
                ", getterId='" + getterId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", content='" + content + '\'' +
                ", sendTime=" + sendTime +
                ", msgType='" + msgType + '\'' +
                ", fileBytes=" + Arrays.toString(fileBytes) +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    public Message(int msgId, String senderId, String getterId, String groupId, String content, Date sendTime, String msgType, byte[] fileBytes, String fileName) {
        this.msgId = msgId;
        this.senderId = senderId;
        this.getterId = getterId;
        this.groupId = groupId;
        this.content = content;
        this.sendTime = sendTime;
        this.msgType = msgType;
        this.fileBytes = fileBytes;
        this.fileName = fileName;
    }

    public Message() {
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
