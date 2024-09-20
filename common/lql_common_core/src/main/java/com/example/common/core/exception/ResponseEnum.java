package com.example.common.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  ResponseEnum implements LqlCommonExceptionAssert{
    /**
     * Bad licence type
     */
    BAD_LICENCE_TYPE("7001", "Bad licence type."),
    /**
     * Licence not found
     */
    LICENCE_NOT_FOUND("7002", "Licence not found."),
    USER_NOT_FOUND("7003", "user not found.")
    ;

    /**
     * 返回码
     */
    private String code;
    /**
     * 返回消息
     */
    private String message;

}
