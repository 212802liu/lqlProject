package com.example.common.security.handler;

import com.example.common.core.web.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * @Validated 参数校验不通过
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult methodArgumentNotValidHandler(MethodArgumentNotValidException  e){
        log.error("参数校验失败！",e);
        String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        return AjaxResult.error(defaultMessage);
    }
}
