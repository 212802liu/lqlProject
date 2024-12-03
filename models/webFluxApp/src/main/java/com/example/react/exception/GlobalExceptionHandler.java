package com.example.react.exception;

import com.example.common.core.exception.LqlCommonException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LqlCommonException.class)
    public String error(LqlCommonException exception){
        System.out.println("发生了系统逻辑异常"+exception.getMessage());

        //返回这些进行错误处理；
//        ProblemDetail：
//        ErrorResponse ：

        return "炸了，哈哈...";
    }
}
