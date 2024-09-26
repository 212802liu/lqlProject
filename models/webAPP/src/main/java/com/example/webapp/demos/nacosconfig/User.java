package com.example.webapp.demos.nacosconfig;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
//@ConfigurationProperties(prefix = "user")
@Component
@Schema(description ="个人信息")
public class User {
    @Schema(description = "名称")
    @Value("${user.name1}")
    String name;
    @Value("${user.age}")
    String age;
}
