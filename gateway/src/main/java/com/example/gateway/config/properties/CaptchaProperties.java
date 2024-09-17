package com.example.gateway.config.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : CaptchaProperties  //类名
 * @Description : 验证码配置  //描述
 * @Author : liuql  //作者
 * @Date: 2024/9/17  11:36
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "security.captcha")
public class CaptchaProperties {
    /**
     * 验证码开关
     */
    private Boolean enabled = true;

    /**
     * 验证码类型（math 数组计算 char 字符）
     */
    private String type = "char";
}
