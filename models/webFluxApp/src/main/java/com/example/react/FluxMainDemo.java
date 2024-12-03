package com.example.react;

import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * 快速自己编写一个能处理请求的服务器
 *
 * example:http://localhost:8080/feign
 */
public class FluxMainDemo {
    public static void main(String[] args) throws IOException {
        //1、创建一个能处理Http请求的处理器。 参数：请求、响应； 返回值：Mono<Void>：代表处理完成的信号
        HttpHandler handler = ( ServerHttpRequest request, ServerHttpResponse response) ->{
            URI uri = request.getURI();
            System.out.println(Thread.currentThread()+"请求进来："+uri);
            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
            String s = "hello ,"+uri;
            DataBuffer data = dataBufferFactory.wrap(s.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(data));

        };
        //2、启动一个服务器，监听8080端口，接受数据，拿到数据交给 HttpHandler 进行请求处理
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(handler);

        //3、启动Netty服务器
        HttpServer.create()
                .host("localhost")
                .port(8080)
                .handle(adapter) //用指定的处理器处理请求
                .bindNow(); //现在就绑定

        System.out.println("服务器启动完成....监听8080，接受请求");
        System.in.read();
        System.out.println("服务器停止....");
    }
}
