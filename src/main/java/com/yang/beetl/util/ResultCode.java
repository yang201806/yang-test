package com.yang.beetl.util;

/**
 * 定义返回数据使用的状态码
 */
public interface ResultCode {

    // 成功
    int SUCCESS = 20000;

    // 失败
    int ERROR = 20001;

    // 没有操作权限状态码
    int AUTH = 30000;
}
