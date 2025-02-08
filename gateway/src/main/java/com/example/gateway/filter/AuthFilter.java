package com.example.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.example.common.core.constant.CommonRedisConstants;
import com.example.common.core.constant.TokenConstants;
import com.example.common.core.util.security.JwtUtils;
import com.example.common.redis.service.RedisClient;
import com.example.gateway.config.properties.IgnoreWhiteProperties;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName : AuthFilter  //类名
 * @Description : 请示token是否有效
 * @Author : liuql  //作者
 * @Date: 2024/9/17  19:53
 */

/**
 * todo: 待测试
 * @RefreshScope 动态刷新 ignoreWhite
 * 当配置中心（如 Spring Cloud Config）中的配置发生变化时，使用 @RefreshScope 注解的 bean 会被重新创建。
 * 这样，bean 中的配置属性会自动更新为最新的值。
 *
 * 或者  , ApplicationListener<EnvironmentChangeEvent>
 * 你可以监听 EnvironmentChangeEvent，在配置更新时手动刷新正则表达式列表。
 */
//@Component
@Slf4j
public class AuthFilter implements GlobalFilter,Ordered {
    /**
     * todo : 配置注入
     */
    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    private List<Pattern> whiteIgnorePatterns;



    //    @Autowired
    private RedisClient redisClient;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();


        // 白名单放行
        if (matches(path)){
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

    private void reFlashWhiteIgnore(){
        whiteIgnorePatterns = ignoreWhite.getWhites().stream().map(Pattern::compile).collect(Collectors.toList());
    }



    public boolean matches(String path) {
        if (CollectionUtils.isEmpty(whiteIgnorePatterns)) {
            reFlashWhiteIgnore();
        }
        for (Pattern pattern : whiteIgnorePatterns) {
            if (pattern.matcher(path).matches()) {
                return true; // 找到匹配的正则表达式
            }
        }
        return false; // 没有匹配
    }

    //    @Override
//    @EventListener
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        reFlashWhiteIgnore();
    }
}