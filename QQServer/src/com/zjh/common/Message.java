package com.zjh.common;

import java.io.Serializable;

/**
 * 消息类
 * @author 张俊鸿
 * @date 2022/05/08
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private String senderId;
    private String getterId;
    private String content;
    private String sendTime;
    private String msgType;
    private FileMsg fileMsg;

    public FileMsg getFileMsg() {
        return fileMsg;
    }

    public void setFileMsg(FileMsg fileMsg) {
        this.fileMsg = fileMsg;
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
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Message(String senderId, String getterId, String content, String sendTime, String msgType) {
        this.senderId = senderId;
        this.getterId = getterId;
        this.content = content;
        this.sendTime = sendTime;
        this.msgType = msgType;
    }

    public Message() {
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender='" + senderId + '\'' +
                ", getter='" + getterId + '\'' +
                ", content='" + content + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", msgType='" + msgType + '\'' +
                ", fileMsg=" + fileMsg +
                '}';
    }
}
