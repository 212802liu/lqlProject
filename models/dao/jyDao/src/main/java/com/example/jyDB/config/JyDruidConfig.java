package com.example.jyDB.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.example.common.core.util.SecureUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @ClassName DruidConfig
 * @Description Druid数据库连接池监控配置
 * http://ip:port/contextpath/druid/login.html
 * @Author Mico
 * @Date 2018/7/19 15:11
 * @Version 1.0
 */
@Configuration(value = "jyDruidConfig")
@ConditionalOnProperty(name = "spring.jy.name")
@Slf4j
@MapperScan(basePackages = "com.example.jyDB.mapper", sqlSessionTemplateRef = "jySqlSessionTemplate")
public class JyDruidConfig {
    /**
     * dao层的包路径
     */
    static final String PACKAGE = "";

    /**
     * mapper文件的相对路径
     */
    private static final String MAPPER_LOCATION = "classpath*:mapper/jy/*.xml";

    @Resource
    private DruidJyConfigProperties druidJyConfigProperties;

    /**
     * @Description 因为有的druid属性默认并不被支持，所以进行配置信息的定制，@Primary是覆盖其它来源的dataSource
     * @Author Mico
     * @Date 2018/7/19 15:52
     * @Param
     * @return
     */
    @Bean(name="jyDataSource")
    public DataSource jyDataSource() {
        String password = "";

        if (StrUtil.isNotBlank(druidJyConfigProperties.getEncryKey())) {
            password = SecureUtils.decryptionWithAES(druidJyConfigProperties.getPassword(),druidJyConfigProperties.getEncryKey());
        }else {
            password = SecureUtils.decryptionWithAES(druidJyConfigProperties.getPassword());

        }

        DruidDataSource dataSource = new DruidDataSource();
//        List filterList = new ArrayList<>();
//        filterList.add(druidJyConfigProperties.getProxyFilters());
//        dataSource.setProxyFilters(filterList);

        dataSource.setName(druidJyConfigProperties.getName());

        dataSource.setDriverClassName(druidJyConfigProperties.getDriverClassName());
        dataSource.setUrl(druidJyConfigProperties.getUrl());
        dataSource.setUsername(druidJyConfigProperties.getUsername());
        dataSource.setPassword(password);

        dataSource.setInitialSize(druidJyConfigProperties.getInitialSize());
        dataSource.setMinIdle(druidJyConfigProperties.getMinIdle());
        dataSource.setMaxActive(druidJyConfigProperties.getMaxActive());
        dataSource.setMaxWait(druidJyConfigProperties.getMaxWait());
        dataSource.setTimeBetweenEvictionRunsMillis(druidJyConfigProperties.getTimeBetweenEvictionRunsMillis());
        dataSource.setMinEvictableIdleTimeMillis(druidJyConfigProperties.getMinEvictableIdleTimeMillis());
        dataSource.setValidationQuery(druidJyConfigProperties.getValidationQuery());
        dataSource.setTestWhileIdle(druidJyConfigProperties.isTestWhileIdle());
        dataSource.setTestOnBorrow(druidJyConfigProperties.isTestOnBorrow());
        dataSource.setTestOnReturn(druidJyConfigProperties.isTestOnReturn());
        dataSource.setPoolPreparedStatements(druidJyConfigProperties.isPoolPreparedStatements());
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(druidJyConfigProperties.getMaxPoolPreparedStatementPerConnectionSize());
        dataSource.setConnectProperties(settingProperties(druidJyConfigProperties.getConnectionProperties()));

        try {
            dataSource.setFilters(druidJyConfigProperties.getFilters());
        } catch (SQLException e) {
            log.error("druid configuration initialization filter", e);
        }

        return dataSource;
    }


    @Bean
    public SqlSessionFactory jySqlSessionFactory(@Qualifier("jyDataSource") DataSource jyDataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(jyDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        return bean.getObject();
    }

    @Bean
    public DataSourceTransactionManager jyTransactionManager(@Qualifier("jyDataSource") DataSource jyDataSource) {
        return new DataSourceTransactionManager(jyDataSource);
    }

    @Bean
    public SqlSessionTemplate jySqlSessionTemplate(@Qualifier("jySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * @Description 将属性connectionProperties解析到Properties对象中
     * @Author Mico
     * @Date 2018/7/19 17:09
     * @Param
     * @return
     */
    private Properties settingProperties(String connectionProperties) {
        Properties properties = new Properties();

        if (connectionProperties != null && !"".equals(connectionProperties.trim())) {
            String[] strArr = connectionProperties.split(";");
            if (strArr.length > 0) {
                for (int i = 0; i < strArr.length; i++) {
                    String str = strArr[i];
                    if (str == null || "".equals(str.trim())) {
                        continue;
                    }
                    else {
                        String[] keyValueArr = str.split("=");
                        if (keyValueArr.length == 2) {
                            properties.setProperty(keyValueArr[0], keyValueArr[1]);
                        }
                    }
                }
            }
        }

        return properties;
    }

}
