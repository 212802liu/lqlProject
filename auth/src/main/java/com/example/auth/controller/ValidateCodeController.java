package com.example.auth.controller;

import com.example.auth.service.ValidateCodeService;
import com.example.common.core.exception.LqlCommonException;
import com.example.common.core.web.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * @ClassName : ValidateCodeController  //类名
 * @Description : 我个人认为验证码这块不应该放在gateway这里，所以新增验证码接口修改
 * @Author : liuql  //作者
 * @Date: 2024/9/17  18:51
 */
@RestController()
@RequestMapping("/Captcha")
public class ValidateCodeController {
    @Autowired
    ValidateCodeService validateCodeService;

    @GetMapping("/code")
    public AjaxResult createCaptcha(){
        AjaxResult ajax = null;
        try
        {
            ajax = validateCodeService.createCaptcha();
        }
        catch (LqlCommonException | IOException e)
        {
//            return Mono.error(e);
        }
        return ajax;
    }
}
