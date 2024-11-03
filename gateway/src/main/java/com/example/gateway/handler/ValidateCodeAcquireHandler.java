package com.example.gateway.handler;

import com.example.common.core.exception.LqlCommonException;
import com.example.common.core.web.AjaxResult;
import com.example.gateway.service.ValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * @ClassName : ValidateCodeAcquireHandler  路由配置在  -> ValidateCodeRouterConfig
 * @Description : 换取验证码处理器  //描述
 * @Author : liuql  //作者
 * @Date: 2024/9/17  11:04
 */
@Component
public class ValidateCodeAcquireHandler implements HandlerFunction<ServerResponse> {
    @Autowired
    private ValidateCodeService validateCodeService;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {

        AjaxResult ajax;
        try
        {
            ajax = validateCodeService.createCaptcha();
        }
        catch (LqlCommonException | IOException e)
        {
            return Mono.error(e);
        }
        return ServerResponse.status(HttpStatus.OK).body(BodyInserters.fromValue(ajax));
    }
}
