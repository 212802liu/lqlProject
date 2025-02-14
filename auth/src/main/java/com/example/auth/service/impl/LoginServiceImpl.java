package com.example.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.example.auth.entity.LoginBody;
import com.example.auth.service.LoginService;
import com.example.common.core.constant.CommonRedisConstants;
import com.example.common.core.entity.user.LoginUser;
import com.example.common.core.exception.ResponseEnum;
import com.example.common.core.web.AjaxResult;
import com.example.common.redis.service.RedisClient;
import com.example.common.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private TokenService tokenService;

    @Override
    public AjaxResult login(LoginBody loginBody) {

        // 数据库
        //模拟查询数据库 无数据
//        ResponseEnum.LICENCE_NOT_FOUND.assertNotNull(null);

        //验证码 ； 成功后作废
        String code =  redisClient.get(CommonRedisConstants.PREFIX_REDIS_CAPTCHA + loginBody.getUuid());
        if (StrUtil.isBlank(code) || !code.equals(loginBody.getValidateCode())){
            return AjaxResult.error("验证码错误或已过期");
        }
        // token
        LoginUser loginUser = new LoginUser();
        String token = tokenService.createToken(loginUser);
        return AjaxResult.success("成功",token);
    }
}
