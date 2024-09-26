package com.example.common.knife.conf;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger 文档配置 : 使用SpringDoc代替Swagger
 */

// 用与 将这个 Knife4jProperties 加载成bean
@EnableConfigurationProperties(SpringDocProperties.class)
@ConditionalOnProperty(name = "springdoc.api-docs.enabled", havingValue = "true", matchIfMissing = true)
public class Swagger2DocAutoConfig {

    /**
     * todo: 待测试
     * @param properties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(OpenAPI.class)
    public OpenAPI openApi(SpringDocProperties properties){
        return new OpenAPI().components(new Components()
                        // 设置认证的请求头
                        .addSecuritySchemes("apikey", securityScheme()))
                .addSecurityItem(new SecurityRequirement().addList("apikey"))
                // 用于设置 API 的基本信息。
                .info(convertInfo(properties.getInfo()))
                // 用于设置 API 的服务器 URL
                .servers(servers(properties.getGatewayUrl()));
    }

    public SecurityScheme securityScheme()
    {
        return new SecurityScheme().type(SecurityScheme.Type.APIKEY)
                .name("Authorization")
                .in(SecurityScheme.In.HEADER)
                .scheme("Bearer");
    }

    private Info convertInfo(SpringDocProperties.InfoProperties infoProperties)
    {
        Info info = new Info();
        info.setTitle(infoProperties.getTitle());
        info.setDescription(infoProperties.getDescription());
        info.setContact(infoProperties.getContact());
        info.setLicense(infoProperties.getLicense());
        info.setVersion(infoProperties.getVersion());
        return info;
    }

    public List<Server> servers(String gatewayUrl)
    {
        List<Server> serverList = new ArrayList<>();
        serverList.add(new Server().url(gatewayUrl));
        return serverList;
    }

}
