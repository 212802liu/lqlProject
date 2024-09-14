package com.example.webapp.demos.nacosconfig;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
//@ConfigurationProperties(prefix = "user")
@Component
public class User {
    @Value("${user.name1}")
    String name;
    @Value("${user.age}")

    String age;
}
