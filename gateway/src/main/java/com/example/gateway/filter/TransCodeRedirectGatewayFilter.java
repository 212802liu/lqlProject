package com.example.gateway.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 实现一个接口 通过不同的 transCode 来重写路径，并路由到合适的服务上 。但是code 必须是明文或者 网关解密
 *
 * 方案二： 在 分发的微服务内部解密，然后Interceptor.preHandle() 中修改请求路径。 但是一定记住： 要在此流程之前完成解密。不能在 advice 中解密。
 * 因为： filter -> Interceptor -> HandlerMapping 分发给 controller 接口 -> ControllerAdvice  ->  controller 接口执行！！！！
 *
 *
 *
 */
@Slf4j
public class TransCodeRedirectGatewayFilter implements GatewayFilter, Ordered {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 读取并缓存请求体
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(dataBuffer -> {
                    // 将请求体转换为字节数组
                    byte[] bytes = readBytesFromDataBuffer(dataBuffer);

                    // 解密逻辑（如果有需要）
                    String requestBody = new String(bytes, StandardCharsets.UTF_8);
                    JsonNode jsonNode = parseJson(requestBody);

                    // 根据 transCode 动态修改路径
                    String transCode = jsonNode.get("transCode").asText();
                    String newPath = getUpdatedPath(exchange.getRequest().getURI().getPath(), transCode);

                    // 创建新的请求对象
                    ServerHttpRequest modifiedRequest = createModifiedRequest(exchange, bytes, newPath);

                    // 使用新的请求对象继续处理
                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                });
    }

    // 从 DataBuffer 中读取字节数组并释放资源
    private byte[] readBytesFromDataBuffer(DataBuffer dataBuffer) {
        byte[] bytes = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(bytes);
        DataBufferUtils.release(dataBuffer);
        return bytes;
    }

    // 将请求体解析为 JsonNode
    private JsonNode parseJson(String requestBody) {
        try {
            return objectMapper.readTree(requestBody);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON: " + e.getMessage(), e);
        }
    }

    // 根据 transCode 动态更新路径
    private String getUpdatedPath(String oldPath, String transCode) {
        return oldPath.replace("common", transCode);
    }

    // 创建修改后的请求对象
    private ServerHttpRequest createModifiedRequest(ServerWebExchange exchange, byte[] bodyBytes, String newPath) {
        ServerHttpRequest originalRequest = exchange.getRequest();

        return new ServerHttpRequestDecorator(
                originalRequest.mutate()
                        .path(newPath) // 替换路径
                        .build()
        ) {
            @Override
            public Flux<DataBuffer> getBody() {
                // 将字节数组包装为 DataBuffer
                return Flux.just(exchange.getResponse().bufferFactory().wrap(bodyBytes));
            }

            // todo ：改这块可能有点问题
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.putAll(originalRequest.getHeaders());
                headers.remove(HttpHeaders.CONTENT_LENGTH); // 移除 Content-Length，框架会重新计算
                return headers;
            }
        };
    }

    @Override
    public int getOrder() {
        return 200;
    }
}
