package com.example.gateway.filter;

import com.example.common.core.util.security.SecureUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 如果请求报文过大或者响应报文过大的时候，这个类的修改请求和响应报文的方法会出现问题！！
 * 当提供的原始请求报文里面字符串长度要大于1000，netty 默认最大读取 1024个。
 *
 * 解决方案： 参考： ModifyRequestBodyGatewayFilterFactory 和 ModifyResponseBodyGatewayFilterFactory
 */
@Slf4j
//@Component
@AllArgsConstructor
public class EncryptionGlobalFilter implements GlobalFilter, Ordered {

    private ObjectMapper objectMapper;



    /**
     * 必须实现Ordered接口，返回一个小于-1的order值，这是因为NettyWriteResponseFilter的order值为-1，
     * 我们需要覆盖返回响应体的逻辑，自定义的GlobalFilter必须比NettyWriteResponseFilter优先执行。
     * @return
     */
    @Override
    public int getOrder() {
        return -2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = request.getURI().getPath();
        boolean flag = path.startsWith("auth");
        if (flag){
            return chain.filter(exchange);
        }
//
//
//        // 白名单放行
//        if (matches(path)){
//            return chain.filter(exchange);
//        }
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        ServerHttpRequestDecorator requestDecorator = processRequest_improve(request, bufferFactory);
        ServerHttpResponseDecorator responseDecorator = processResponse_improve(response, bufferFactory);
        return chain.filter(exchange.mutate().request(requestDecorator).response(responseDecorator).build());
    }

    /**
     * 问题原因：
     * request.getBody() 返回的是 Flux<DataBuffer>，表示一个异步数据流。
     * 你通过 body.subscribe(...) 订阅了这个流，但 订阅操作是非阻塞的，
     * 主线程会继续执行后续代码，而此时数据可能尚未完全读取。
     */
    private ServerHttpRequestDecorator processRequest(ServerHttpRequest request, DataBufferFactory bufferFactory) {

        DataBufferHolder holder = new DataBufferHolder();
        log.info("开始加密！");
        Flux<DataBuffer> body = request.getBody();
        body.subscribe(dataBuffer -> {
            int len = dataBuffer.readableByteCount();
            holder.length = len;
            byte[] bytes = new byte[len];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            String text = new String(bytes, StandardCharsets.UTF_8);
            JsonNode jsonNode = readNode(text);
            JsonNode payload = jsonNode.get("password");
            String payloadText = payload.asText();
            byte[] content = SecureUtils.decryption(payloadText);
            String requestBody = new String(content, StandardCharsets.UTF_8);
            log.info("修改请求体payload,修改前:{},修改后:{}", payloadText, requestBody);
            rewritePayloadNode(requestBody, jsonNode);
            DataBuffer data = bufferFactory.allocateBuffer();
            data.write(jsonNode.toString().getBytes(StandardCharsets.UTF_8));
            holder.dataBuffer = data;
        });
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(request.getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        return new ServerHttpRequestDecorator(request) {

            @Override
            public HttpHeaders getHeaders() {
                int contentLength = holder.length;
                if (contentLength > 0) {
                    headers.setContentLength(contentLength);
                } else {
                    headers.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                return headers;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return Flux.just(holder.dataBuffer);
            }
        };
    }


    private ServerHttpRequestDecorator processRequest_improve(ServerHttpRequest request, DataBufferFactory bufferFactory) {
        // 将请求体处理逻辑缓存，确保只消费一次
        Mono<DataBuffer> processedBody = DataBufferUtils.join(request.getBody())
                // 通过 flatMap 确保在数据完全加载后才执行后续逻辑。
                .flatMap(dataBuffer -> {
                    try {
                        // 读取原始数据
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        DataBufferUtils.release(dataBuffer);

                        // 解密并修改请求体
                        String text = new String(bytes, StandardCharsets.UTF_8);
                        JsonNode jsonNode = readNode(text);
                        JsonNode payload = jsonNode.get("password");
                        String payloadText = payload.asText();
                        byte[] content = SecureUtils.decryption(payloadText);
                        String requestBody = new String(content, StandardCharsets.UTF_8);
                        log.info("修改请求体payload,修改前:{},修改后:{}", payloadText, requestBody);
                        rewritePayloadNode(requestBody, jsonNode);

                        // 生成新数据
                        byte[] newBodyBytes = jsonNode.toString().getBytes(StandardCharsets.UTF_8);
                        DataBuffer newBuffer = bufferFactory.allocateBuffer(newBodyBytes.length);
                        newBuffer.write(newBodyBytes);
                        return Mono.just(newBuffer);
                    } catch (Exception e) {
                        DataBufferUtils.release(dataBuffer);
                        log.error("修改请求体异常！",e);
                        return Mono.error(e);
                    }
                });
//                .cache(); // 缓存结果，避免重复处理

        // 构建新的请求装饰器
        return new ServerHttpRequestDecorator(request) {
            @Override
            public HttpHeaders getHeaders() {
                // 动态计算 Content-Length（非阻塞方式）
                HttpHeaders headers = new HttpHeaders();
                headers.putAll(request.getHeaders());
                headers.remove(HttpHeaders.CONTENT_LENGTH);
                return headers;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                //  Flux.from(processedBody)。
                return processedBody.flux(); // 将 Mono 转换为 Flux
            }
        };
    }
    private ServerHttpResponseDecorator processResponse(ServerHttpResponse response, DataBufferFactory bufferFactory) {
        return new ServerHttpResponseDecorator(response) {

            @SuppressWarnings("unchecked")
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> flux = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(flux.map(buffer -> {
                        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
                        DataBufferUtils.release(buffer);
                        JsonNode jsonNode = readNode(charBuffer.toString());
                        JsonNode payload = jsonNode.get("password");
                        String text = payload.toString();
                        String content = SecureUtils.encryptionWithAES(text);
                        log.info("修改响应体payload,修改前:{},修改后:{}", text, content);
                        setPayloadTextNode(content, jsonNode);
                        return bufferFactory.wrap(jsonNode.toString().getBytes(StandardCharsets.UTF_8));
                    }));
                }
                return super.writeWith(body);
            }
        };
    }

    private ServerHttpResponseDecorator processResponse_improve(ServerHttpResponse response, DataBufferFactory bufferFactory) {
        return new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> flux = (Flux<? extends DataBuffer>) body;

                    // 聚合所有 DataBuffer 为单个处理
                    return DataBufferUtils.join(flux)
                            .flatMap(buffer -> {
                                try {
                                    // 读取完整响应内容
                                    String originalContent = buffer.toString(StandardCharsets.UTF_8);
                                    JsonNode jsonNode = readNode(originalContent);

                                    // 修改密码字段
                                    JsonNode payload = jsonNode.get("password");
                                    if (payload != null) {
                                        String encrypted = SecureUtils.encryptionWithAES(payload.asText());
                                        ((ObjectNode) jsonNode).put("password", encrypted);
                                        log.info("修改响应体payload,修改前:{},修改后:{}", payload.asText(), encrypted);
                                    }

                                    // 生成新数据
                                    byte[] newBytes = jsonNode.toString().getBytes(StandardCharsets.UTF_8);
                                    DataBuffer newBuffer = bufferFactory.wrap(newBytes);
                                    DataBufferUtils.release(buffer); // 释放原 buffer

                                    // 写入新数据
                                    return super.writeWith(Mono.just(newBuffer));
                                } catch (Exception e) {
                                    DataBufferUtils.release(buffer);
                                    log.error("处理响应体失败", e);
                                    return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "网关处理响应失败"));
                                }
                            });
                }
                return super.writeWith(body);
            }
        };
    }
    private void rewritePayloadNode(String text, JsonNode root) {
        try {
            TextNode node = objectMapper.getNodeFactory().textNode(text);
            ObjectNode objectNode = (ObjectNode) root;
            objectNode.set("password", node);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void setPayloadTextNode(String text, JsonNode root) {
        try {
            ObjectNode objectNode = (ObjectNode) root;
            objectNode.set("password", new TextNode(text));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private JsonNode readNode(String in) {
        try {
            return objectMapper.readTree(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private class DataBufferHolder {

        DataBuffer dataBuffer;
        int length;
    }
}
