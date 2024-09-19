package com.example.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.example.auth.entity.LoginBody;
import com.example.auth.service.LoginService;
import com.example.common.core.constant.CommonRedisConstants;
import com.example.common.core.entity.user.LoginUser;
import com.example.common.core.util.JwtUtils;
import com.example.common.core.web.AjaxResult;
import com.example.common.redis.service.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private RedisClient redisClient;

    @Override
    public AjaxResult login(LoginBody loginBody) {

        // 数据库

        //验证码 ； 成功后作废
        String code =  redisClient.get(CommonRedisConstants.PREFIX_REDIS_CAPTCHA + loginBody.getUuid());
        if (StrUtil.isBlank(code) || !code.equals(loginBody.getValidateCode())){
            AjaxResult.error("验证码错误或已过期");
        }
        // token
        LoginUser loginUser = new LoginUser();
        String token = JwtUtils.createToken(loginUser);
        loginUser.setToken(token);

        return AjaxResult.success("成功",loginUser);
    }
}
