package com.zjh.common;

/**
 * @author 张俊鸿
 * @description: 响应状态码
 * @since 2022-05-10 22:20
 */
public interface StateCode {
    String SUCCEED = "200"; //操作成功
    String FAIL = "500"; //操作失败
    String HAS_LOGIN = "501"; //用户已经登录，不可重复登录
    String NOT_FOUND = "404"; //找不到请求的路径
}
