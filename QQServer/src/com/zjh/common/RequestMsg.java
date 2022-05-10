package com.zjh.common;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author 张俊鸿
 * @description: 请求消息包
 * @since 2022-05-10 22:09
 */
public class RequestMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    /**请求者的id**/
    String requesterId;
    /**请求内容:传后端的方法名**/
    String content;
    /**方法传参**/
    private Object[] params;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

    public RequestMsg() {
    }

    public RequestMsg(String requesterId, String content, Object[] params) {
        this.requesterId = requesterId;
        this.content = content;
        this.params = params;
    }

    @Override
    public String toString() {
        return "RequestMsg{" +
                "content='" + content + '\'' +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
