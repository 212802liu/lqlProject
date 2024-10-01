package com.example.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : SwaggerProvider
 * @Description : swagger
 * @Author : liuql
 * @Date: 2024/10/1  11:18
 * 文档地址： http://localhost:8081/swagger-ui/index.html
 *
 * SwaggerResourcesProvider：用于提供 Swagger 资源的接口，允许聚合多个服务的 Swagger 文档。
 * WebFluxConfigurer：用于配置 Spring WebFlux 的功能。
 */
@Component
public class SwaggerProvider implements SwaggerResourcesProvider, WebFluxConfigurer {
    /**
     * Swagger2默认的url后缀
     */
    public static final String SWAGGER2URL = "/v3/api-docs";

    /**
     * 网关路由
     * RouteLocator 是一个接口，用于定义动态路由。它的实现通常用于程序化地创建和管理路由。 不包括静态路由(但实测包括)
     * 当你需要根据某些条件动态生成路由时，可以使用 RouteLocator。你可以在代码中定义路由及其规则，而不是仅依赖配置文件。
     * @Component
     * public class DynamicRouteConfig {
     *
     *     private final RouteLocatorBuilder builder;
     *
     *     public DynamicRouteConfig(RouteLocatorBuilder builder) {
     *         this.builder = builder;
     *     }
     *
     *     @Bean
     *     public RouteLocator dynamicRoutes() {
     *         return builder.routes()
     *                 .route("dynamic_route", r -> r.path("/dynamic/**")
     *                         .uri("http://dynamic.example.com"))
     *                 .build();
     *     }
     * }
     */
    @Lazy
    @Autowired
    private RouteLocator routeLocator;

    /**
     * GatewayProperties 是 Spring Cloud Gateway 的配置属性类，
     * 通常从配置文件（如 application.yml 或 application.properties）中加载静态路由
     */
    @Autowired
    private GatewayProperties gatewayProperties;

    /**
     *
     *       routes: # 网关路由配置
     *         - id: baidu # 路由id, 自定义，唯一即可
     *           # uri: 127.0.0.1:/order # - 路由目的地，支持lb和http两种
     *           uri: lb://lql-web # 路由的目的地，lb是负载均衡，后面跟服务名称
     *           predicates: # 路由断言，也就是判断是否符合路由规则的条件
     *             - Path=/webapi/** # path 按照路径进行匹配，只要以/order-service/开头就符合规则
     * 聚合其他服务接口
     *
     * @return
     */
    @Override
    public List<SwaggerResource> get()
    {
        List<SwaggerResource> resourceList = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        // 获取网关中配置的route
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        gatewayProperties.getRoutes().stream()
                // 筛选出动态路由
                .filter(routeDefinition -> routes
                        .contains(routeDefinition.getId()))
                .forEach(routeDefinition -> routeDefinition.getPredicates().stream()
                        // 使用path 作为 predicate
                        .filter(predicateDefinition -> "Path".equalsIgnoreCase(predicateDefinition.getName()))
                        // 不包括lql-auth 权限模块
                        .filter(predicateDefinition -> !"lql-auth".equalsIgnoreCase(routeDefinition.getId()))
                        .forEach(predicateDefinition -> resourceList
                                .add(swaggerResource(routeDefinition.getId(), predicateDefinition.getArgs()
                                        .get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("/**", SWAGGER2URL)))));
        return resourceList;
    }

    /**
     *
     * 方法说明：用于创建一个 SwaggerResource 实例。
     * 参数：
     *  name：路由的名称。
     *  location：Swagger 文档的 URL。
     * 返回：返回配置好的 SwaggerResource 实例。
     */
    private SwaggerResource swaggerResource(String name, String location)
    {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }

    /**
     * addResourceHandler("/swagger-ui/**")：定义资源处理的路径。
     * addResourceLocations(...)：指定 Swagger UI 的资源位置，通常是 Springfox 提供的 webjars。
     *
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** swagger-ui 地址 */
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }
}
