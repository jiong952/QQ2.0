package com.zjh.common;

import java.io.Serializable;

/**
 * @author 张俊鸿
 * @description: 响应消息包
 * @since 2022-05-10 22:12
 */
public class ResponseMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    /**响应码 200成功..**/
    String stateCode;
    /**响应内容：放方法返回值**/
    private Object returnValue;

    public ResponseMsg(String stateCode, Object returnValue) {
        this.stateCode = stateCode;
        this.returnValue = returnValue;
    }

    public ResponseMsg() {
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }
}
