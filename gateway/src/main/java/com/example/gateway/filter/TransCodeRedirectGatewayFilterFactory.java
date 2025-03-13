package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransCodeRedirectGatewayFilterFactory extends AbstractGatewayFilterFactory<TransCodeRedirectGatewayFilterFactory.Config> {


    public TransCodeRedirectGatewayFilterFactory() {
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {

        return  new TransCodeRedirectGatewayFilter();
    }


    public static class Config {
        // 可以在这里定义配置参数
    }
}
