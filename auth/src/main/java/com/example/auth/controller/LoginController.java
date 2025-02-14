package com.example.auth.controller;

import com.example.auth.entity.LoginBody;
import com.example.auth.service.LoginService;
import com.example.common.core.web.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @ClassName : LoginController  //类名
 * @Description : 登入  //描述
 * @Author : liuql  //作者
 * @Date: 2024/9/17  18:42
 */
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping("/login")
    private AjaxResult login(@Validated @RequestBody LoginBody loginBody){
        AjaxResult ajaxResult = loginService.login(loginBody);
        return ajaxResult;
    }
}
