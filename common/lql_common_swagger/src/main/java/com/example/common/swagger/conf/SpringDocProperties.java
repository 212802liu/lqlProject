package com.example.common.swagger.conf;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.info.InfoProperties;

@Data
@ConfigurationProperties(prefix = "springdoc")
public class SpringDocProperties {
    /**
     * 网关
     */
    private String gatewayUrl;

    /**
     * 文档基本信息
     */
    @NestedConfigurationProperty
    private InfoProperties info = new InfoProperties();

    /**
     * <p>
     * 文档的基础属性信息
     * </p>
     *
     *
     * 为了 springboot 自动生产配置提示信息，所以这里复制一个类出来
     */
    @Data
    public static class InfoProperties {

        /**
         * 标题
         */
        private String title = null;

        /**
         * 描述
         */
        private String description = null;

        /**
         * 联系人信息
         * <p>
         * 在Spring Boot中，@NestedConfigurationProperty注解主要用于标记配置类中的复杂属性，
         * 特别是当属性类型为自定义类型或包含嵌套属性时。这个注解指示Spring Boot的配置绑定机制，该属性应被视为嵌套的配置对象，并且需要进行适当的处理。
         * <p>
         * 大概理解为，当配置类的数据结构比较复杂时，比如说一层嵌套一层，或者有List，Map这种结构的，需要使用@NestedConfigurationProperty注解完成配置。
         */
        @NestedConfigurationProperty
        private Contact contact = null;

        /**
         * 许可证
         */
        @NestedConfigurationProperty
        private License license = null;

        /**
         * 版本
         */
        private String version = null;
    }

}
