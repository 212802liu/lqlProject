package com.example.gateway.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger.web.*;

import java.util.Optional;

@RestController
@RequestMapping("/swagger-resources")
public class SwaggerHandler
{
    // 使用 @Autowired(required = false) 表示如果这些依赖不可用，程序不会抛出异常。
    @Autowired(required = false)
    private SecurityConfiguration securityConfiguration;

    @Autowired(required = false)
    private UiConfiguration uiConfiguration;

    private final SwaggerResourcesProvider swaggerResources;

    @Autowired
    public SwaggerHandler(SwaggerResourcesProvider swaggerResources)
    {
        this.swaggerResources = swaggerResources;
    }

    /**
     * 返回一个 Mono<ResponseEntity<SecurityConfiguration>>，这是一个响应实体，包含当前的安全配置。
     * 如果 securityConfiguration 为 null，则创建一个默认的安全配置。
     * @return
     */
    @GetMapping("/configuration/security")
    public Mono<ResponseEntity<SecurityConfiguration>> securityConfiguration()
    {
        return Mono.just(new ResponseEntity<>(
                Optional.ofNullable(securityConfiguration).orElse(SecurityConfigurationBuilder.builder().build()),
                HttpStatus.OK));
    }

    /**
     * 返回一个 Mono<ResponseEntity<UiConfiguration>>，包含当前的 UI 配置。
     * 如果 uiConfiguration 为 null，则创建一个默认的 UI 配置。
     * @return
     */
    @GetMapping("/configuration/ui")
    public Mono<ResponseEntity<UiConfiguration>> uiConfiguration()
    {
        return Mono.just(new ResponseEntity<>(
                Optional.ofNullable(uiConfiguration).orElse(UiConfigurationBuilder.builder().build()), HttpStatus.OK));
    }

    /**
     * @SuppressWarnings("rawtypes") // 抑制与原始类型相关的警告
     *     public static void useRawType() {
     *         List list = new ArrayList(); // 使用原始类型
     *         list.add("Hello");
     *         list.add(123); // 可以添加不同类型的元素
     *
     *         for (Object obj : list) {
     *             System.out.println(obj); // 输出每个元素
     *         }
     * @return
     *
     * 返回一个 Mono<ResponseEntity>，包含 Swagger 资源列表，状态码为 200 OK。
     */
    @SuppressWarnings("rawtypes")
    @GetMapping("")
    public Mono<ResponseEntity> swaggerResources()
    {
        return Mono.just((new ResponseEntity<>(swaggerResources.get(), HttpStatus.OK)));
    }
}
