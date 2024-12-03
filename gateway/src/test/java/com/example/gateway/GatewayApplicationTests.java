package com.example.gateway;

import com.example.jyDB.domain.JyViraccAgreementStatus;
import com.example.jyDB.mapper.JyViraccAgreementStatusMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class GatewayApplicationTests {

@Autowired
    JyViraccAgreementStatusMapper jyViraccAgreementStatusMapper;
    @Autowired
    private ApplicationContext applicationContext;

    @Value("${spring.jy.name}")
//    @Value("${spring.redis.database}")
//    @Value("${lql.name}")
    private String nn;

    @Test
    void contextLoads() {
        System.out.println(nn);
        JyViraccAgreementStatus status = jyViraccAgreementStatusMapper.selectByPrimaryKey("TS1311006957671891010GGL");
        System.out.println(status);
    }

}
