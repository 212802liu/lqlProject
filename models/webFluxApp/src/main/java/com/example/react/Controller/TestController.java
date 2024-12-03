package com.example.react.Controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@ResponseBody
@Controller
public class TestController {
    //WebFlux： 向下兼容原来SpringMVC的大多数注解和API；

    /**
     * 可传的入参： https://docs.spring.io/spring-framework/docs/5.2.25.RELEASE/spring-framework-reference/web-reactive.html#spring-webflux 1.4.3 下的Method Arguments
     * 这里似乎是因为参数太多的原因，不能使用
     * @param key
     * @param exchange
     * @param webSession
     * @param method
     * @param entity
     * @param s
     * @param file
     * @return
     */
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "key",required = false,defaultValue = "哈哈") String key,
                        ServerWebExchange exchange,
                        WebSession webSession,
                        HttpMethod method,
                        HttpEntity<String> entity,
                        @RequestBody String s,
                        FilePart file){

//        file.transferTo() //零拷贝技术；
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String name = method.name();



        Object aaa = webSession.getAttribute("aaa");
        webSession.getAttributes().put("aa","nn");

        return "Hello World!!! key="+key;
    }


    /**
     * Rendering：一种视图对象。
     * 需要去掉@ResponseBody注解,还是不行，靠
      */

    @GetMapping("/baidu")
    public Rendering render(){
//        Rendering.redirectTo("/aaa"); //重定向到当前项目根路径下的 aaa
        return   Rendering.redirectTo("https://www.baidu.com").build();
    }


    @GetMapping("/test")
    public String test(){
//        ResponseEnum.LICENCE_NOT_FOUND.assertNotNull(null);
        if(true)
        throw new RuntimeException("hhhh");
//        Rendering.redirectTo("/aaa"); //重定向到当前项目根路径下的 aaa
        return  "hhhh";
    }

    //text/event-stream
    /**
     * 服务端推送，也称为消息推送或通知推送，是一种允许应用服务器主动将信息发送到客户端的能力，为客户端提供了实时的信息更新和通知，增强了用户体验。
     *  SSE测试； chatgpt都在用； 服务端推送
     */

    @GetMapping(value = "/sse",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> sse(){
        return Flux.range(1,10)
                .map(i-> {
                    //构建一个SSE对象
                    return    ServerSentEvent.builder("ha-" + i)
                            .id(i + "")
                            .comment("hei-" + i)
                            .event("haha")
                            .build();
                })
                .delayElements(Duration.ofMillis(500));
    }

    private ExecutorService nonBlockingService = Executors.newCachedThreadPool();

    @GetMapping("/sse2")
    public SseEmitter getSseStream() {
        SseEmitter emitter = new SseEmitter();

        nonBlockingService.execute(() -> {
            // 这里模拟数据发送给客户端的逻辑
            try {
                for (int i = 0; i < 10; i++) {
                    emitter.send("Data: " + i);
                    Thread.sleep(1000);
                }
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });

        return emitter;
    }
}
