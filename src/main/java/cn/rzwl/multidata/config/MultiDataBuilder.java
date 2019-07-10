package cn.rzwl.multidata.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : championjing
 * @ClassName: MultiDataBuilder
 * @Description:
 * @Date: 7/10/2019 3:28 PM
 */
@Configuration
@EnableTransactionManagement
@Slf4j
public class MultiDataBuilder {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.test1")
    public DataSource test1(){
        log.info("构建test1数据源");
        DataSource source = DruidDataSourceBuilder.create().build();
        return source;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.test2")
    public DataSource test2(){
        log.info("构建test2--- 数据源");
        DataSource source = DruidDataSourceBuilder.create().build();
        return source;
    }


}
