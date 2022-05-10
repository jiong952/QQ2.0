package com.zjh.client.service;

/**
 * @author 张俊鸿
 * @description: service异常类
 * @since 2022-05-10 23:03
 */
public class ServiceException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ServiceException() {
    }


    public ServiceException(String message) {
        super(message);
    }


    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }


    public ServiceException(Throwable cause) {
        super(cause);
    }


    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
