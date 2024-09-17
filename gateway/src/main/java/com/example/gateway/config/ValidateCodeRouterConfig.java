package com.example.gateway.config;

import com.example.gateway.handler.ValidateCodeAcquireHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * @ClassName : ValidateCodeRouterConfig  //类名
 * @Description : 给网关直接处理获取验证码的请求，而不用搭建一个web环境
 *  * 路由配置信息
 * @Author : liuql  //作者
 * @Date: 2024/9/17  10:58
 */
@Configuration
public class ValidateCodeRouterConfig {
    @Autowired
    private ValidateCodeAcquireHandler validateCodeAcquireHandler;
    @Bean
    public RouterFunction ValidateCodeRouterFunction(){
        return RouterFunctions.
                route(RequestPredicates.GET("/code").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                        validateCodeAcquireHandler);
    }

}
