package com.example.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableDiscoveryClient

//@EnableDiscoveryClient
public class WebAppApplication {
    int a;

    public static void main(String[] args) {
        SpringApplication.run(WebAppApplication.class, args);
    }

}
