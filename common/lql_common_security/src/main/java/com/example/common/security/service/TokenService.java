package com.example.common.security.service;

import cn.hutool.core.lang.UUID;
import com.example.common.core.constant.SecurityConstants;
import com.example.common.core.entity.user.LoginUser;
import com.example.common.core.util.security.JwtUtils;
import com.example.common.redis.service.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class TokenService {
    @Autowired
    private RedisClient redisClient;

    private final static Long expireTime = 60L;
    private final static String  ACCESS_TOKEN = "login:token:";


    public  String createToken(LoginUser loginUser) {
        Map<String,Object> claims = new HashMap<>();
        claims.put(SecurityConstants.DETAILS_USER_ID,loginUser.getUserid());
        claims.put(SecurityConstants.DETAILS_USERNAME,loginUser.getUsername());
        /**
         * 需要一个变量，来保证用户每次登入获取的 token 都是不一致的。
         */
        String uuid = UUID.fastUUID().toString();
        claims.put(SecurityConstants.USER_KEY,uuid);

        loginUser.setUuid(uuid);
        initToken(loginUser);
        return JwtUtils.createToken(claims);
    }

    /**
     * 初始化 令牌redis有效期
     *
     */
    public void initToken(LoginUser loginUser)
    {

        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getUuid());

        redisClient.setCacheObject(userKey,loginUser,  expireTime, TimeUnit.MINUTES);
    }

    /**
     * 刷新令牌有效期
     *
     */
    public void refreshToken(String token)
    {
        String uuid = JwtUtils.getUserKey(token);

        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(uuid);

        redisClient.expire(userKey,  expireTime, TimeUnit.MINUTES);
    }

    private String getTokenKey(String token)
    {
        return ACCESS_TOKEN + token;
    }
}
