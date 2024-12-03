package com.example.jyDB.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @ClassName DruidConfigProperties
 * @Description 获取druid配置属性信息
 * @Author Mico
 * @Date 2018/7/19 15:56
 * @Version 1.0
 */
@Component(value = "druidJyConfigProperties")
@ConditionalOnProperty(name = "spring.jy.name")
@Data
public class DruidJyConfigProperties {

    @Value("${spring.jy.type}")
    private String type;

    @Value("${spring.jy.name}")
    private String name;

    @Value("${spring.jy.driver-class-name}")
    private String driverClassName;

    @Value("${spring.jy.filters}")
    private String filters;

    @Value("${spring.jy.initial-size}")
    private int initialSize;

    @Value("${spring.jy.min-idle}")
    private int minIdle;

    @Value("${spring.jy.max-active}")
    private int maxActive;

    @Value("${spring.jy.max-wait}")
    private int maxWait;

    @Value("${spring.jy.validation-query}")
    private String validationQuery;

    @Value("${spring.jy.test-on-borrow}")
    private boolean testOnBorrow;

    @Value("${spring.jy.test-on-return}")
    private boolean testOnReturn;

    @Value("${spring.jy.test-while-idle}")
    private boolean testWhileIdle;

    @Value("${spring.jy.time-between-eviction-runs-millis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.jy.min-evictable-idle-time-millis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.jy.remove-abandoned}")
    private boolean removeAbandoned;

    @Value("${spring.jy.remove-abandoned-timeout}")
    private int removeAbandonedTimeout;

    @Value("${spring.jy.log-abandoned}")
    private boolean logAbandoned;

    @Value("${spring.jy.pool-prepared-statements}")
    private boolean poolPreparedStatements;

    @Value("${spring.jy.max-pool-prepared-statement-per-connection-size}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${spring.jy.connection-properties}")
    private String connectionProperties;

    @Value("${spring.jy.druid.url}")
    private String url;

    @Value("${spring.jy.druid.username}")
    private String username;

    @Value("${spring.jy.druid.password}")
    private String password;

    @Value("${spring.jy.druid.encryKey:}")
//    @Value("${spring.jy.druid.encryKey:@null}")
    private String encryKey;




}
