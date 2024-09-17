package com.example.common.core.exception;

/**
 * @ClassName : LqlCommonException  //类名
 * @Description : 通用异常抛出类  //描述
 * @Author : liuql  //作者
 * @Date: 2024/9/17  11:27
 */

public class LqlCommonException extends RuntimeException{
    String errorCode ;

    public LqlCommonException(String message) {
        super(message);
        this.errorCode = "001";
    }

    public LqlCommonException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public LqlCommonException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
