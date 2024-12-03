package com.example.react.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class MyWebConfig {
    /**
     * 跨域配置
     * @return
     */
    @Bean
    public WebFluxConfigurer webFluxConfigurer(){
            return new WebFluxConfigurer() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/**")
                            .allowedHeaders("*")
                            .allowedMethods("*")
                            .allowedOrigins("localhost");
                }
            };
    }
}
