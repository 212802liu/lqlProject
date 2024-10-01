package com.example.webapp.demos.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
//@ConfigurationProperties(prefix = "user")
@Component
@Schema(description ="个人信息")
public class User {
    @Schema(description = "名称",example="小明")
    @Value("${user.name1}")
    String name;

    @Schema(description = "年龄",example="12")
    @Value("${user.age}")
    String age;
}
