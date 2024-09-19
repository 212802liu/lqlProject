package com.example.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.example.common.core.constant.CommonRedisConstants;
import com.example.common.core.constant.TokenConstants;
import com.example.common.core.util.JwtUtils;
import com.example.common.redis.service.RedisClient;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Constants;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @ClassName : AuthFilter  //类名
 * @Description : 请示token是否有效
 * @Author : liuql  //作者
 * @Date: 2024/9/17  19:53
 */
@Component
@Slf4j
public class AuthFilter implements GlobalFilter,Ordered {
    /**
     * todo : 配置注入
     */
    private List<String> whiteIgnore;

    @Autowired
    private RedisClient redisClient;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        // 白名单放行
        if (whiteIgnore.contains(path)){
            return chain.filter(exchange);
        }
        // 验证taken
        String token = getToken(request);
        if (StrUtil.isBlank(token)) {
            return unauthorizedResponse(exchange,"token 令牌为空！");
        }
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null)
        {
            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
        }
        String userkey = JwtUtils.getUserKey(claims);
        // 从redis 获取信息，对比
        if (redisClient.hasKey(CommonRedisConstants.PREFIX_REDIS_TOKEN+token)) {

        }
        String userid = JwtUtils.getUserId(claims);
        String username = JwtUtils.getUserName(claims);

        /**
         * todo: 暂时不清楚有什么意义
         */
        // 设置用户信息到请求
        /*addHeader(mutate, SecurityConstants.USER_KEY, userkey);
        addHeader(mutate, SecurityConstants.DETAILS_USER_ID, userid);
        addHeader(mutate, SecurityConstants.DETAILS_USERNAME, username);
        // 内部请求来源参数清除
        removeHeader(mutate, SecurityConstants.FROM_SOURCE);
        return chain.filter(exchange.mutate().request(mutate.build()).build());*/


        return chain.filter(exchange);
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) throws UnsupportedEncodingException {
        if (value == null)
        {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = URLEncoder.encode(valueStr, StandardCharsets.UTF_8.displayName());
        mutate.header(name, valueEncode);
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String s) {
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
        return null;
//        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED);
    }



    @Override
    public int getOrder() {
        return 0;
    }

    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StrUtil.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX))
        {
            token = token.replaceFirst(TokenConstants.PREFIX,"");
        }
        return token;
    }


}
