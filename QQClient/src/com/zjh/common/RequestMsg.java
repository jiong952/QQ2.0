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

    public RequestMsg() {
    }

    public RequestMsg(String content, Object[] params) {
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
