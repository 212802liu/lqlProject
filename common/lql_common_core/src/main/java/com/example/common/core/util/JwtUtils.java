package com.example.common.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.example.common.core.constant.SecurityConstants;
import com.example.common.core.constant.TokenConstants;
import com.example.common.core.entity.user.LoginUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @ClassName : JwtUtils  //类名
 * @Description : jwt 工具来  //描述
 * @Author : liuql  //作者
 * @Date: 2024/9/17  20:16
 */

public class JwtUtils {
    public static String secret = TokenConstants.SECRET;


    public static String createToken(LoginUser loginUser) {
        Map<String,Object> claims = new HashMap<>();
        claims.put(SecurityConstants.DETAILS_USER_ID,loginUser.getUserid());
        claims.put(SecurityConstants.DETAILS_USERNAME,loginUser.getUsername());
        /**
         * 需要一个变量，来保证用户每次登入获取的 token 都是不一致的。
         */
        String uuid = UUID.fastUUID().toString();
        claims.put(SecurityConstants.USER_KEY,uuid);

        return createToken(claims);
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    public static String createToken(Map<String, Object> claims)
    {
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public static Claims parseToken(String token)
    {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 根据令牌获取用户标识
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserKey(String token)
    {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据令牌获取用户标识
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getUserKey(Claims claims)
    {
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据令牌获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserId(String token)
    {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    /**
     * 根据身份信息获取用户ID
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getUserId(Claims claims)
    {
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    /**
     * 根据令牌获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public static String getUserName(String token)
    {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }

    /**
     * 根据身份信息获取用户名
     *
     * @param claims 身份信息
     * @return 用户名
     */
    public static String getUserName(Claims claims)
    {
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }

    /**
     * 根据身份信息获取键值
     *
     * @param claims 身份信息
     * @param key 键
     * @return 值
     */
    public static String getValue(Claims claims, String key)
    {
        return StrUtil.toString(claims.getOrDefault(key,""));
    }

}
