package com.example.gateway.service;

import com.example.common.core.exception.LqlCommonException;
import com.example.common.core.web.AjaxResult;

import java.io.IOException;

/**
 * @author:liuql
 * @create: 2024-09-17 11:11
 * @Description: 验证码服务类
 */
public interface ValidateCodeService {
    /**
     * 生成验证码
     */
    public AjaxResult createCaptcha() throws IOException, LqlCommonException;

    /**
     * 校验验证码
     */
    public void checkCaptcha(String key, String value) throws LqlCommonException;
}
