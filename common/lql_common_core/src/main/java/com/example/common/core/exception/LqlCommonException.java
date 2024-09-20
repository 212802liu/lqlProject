package com.example.common.core.exception;

import com.example.common.core.constant.ResponseConstants;
import com.example.common.core.exception.commonHandler.IResponseEnum;
import lombok.Data;

/**
 * @ClassName : LqlCommonException  //类名
 * @Description : 通用异常抛出类  //描述
 * @Author : liuql  //作者
 * @Date: 2024/9/17  11:27
 *
 *
 */
@Data
public class LqlCommonException extends RuntimeException {
    String errorCode ;

    public LqlCommonException(String message) {
        super(message);
        this.errorCode = ResponseConstants.HANDLE_FAILED;
    }

//    public LqlCommonException(String message, String errorCode) {
//        super(message);
//        this.errorCode = errorCode;
//    }

    public LqlCommonException(IResponseEnum responseEnum) {
        super(responseEnum.getMessage());
        this.errorCode = responseEnum.getCode();
    }


    public LqlCommonException(IResponseEnum responseEnum, Throwable cause) {
        super(responseEnum.getMessage(), cause);
        this.errorCode = ResponseConstants.HANDLE_FAILED;
    }

    public LqlCommonException(Throwable cause) {
        super(cause);
        this.errorCode = ResponseConstants.HANDLE_FAILED;
    }
}
