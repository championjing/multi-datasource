package cn.rzwl.multidata.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author : championjing
 * @ClassName: MultiDataBuilder
 * @Description:
 * @Date: 7/10/2019 3:28 PM
 */
@Configuration
@Slf4j
public class MultiDataBuilder {
    
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.test1")
    public DataSource test1DB(){
        log.info("构建test1数据源");
        DataSource source = DruidDataSourceBuilder.create().build();
        return source;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.test2")
    public DataSource test2DB(){
        log.info("构建test2--- 数据源");
        DataSource source = DruidDataSourceBuilder.create().build();
        return source;
    }

    @Bean
    public DataSource addSource(@Autowired DataSource test1DB, @Autowired DataSource test2DB){
        DBAssistant assistant = new DBAssistant();
        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put(DBAssistant.SourceName.TEST1.dbName,test1DB);
        dataSources.put(DBAssistant.SourceName.TEST2.dbName,test2DB);
        assistant.setTargetDataSources(dataSources);
        assistant.setDefaultTargetDataSource( test1DB );
        return assistant;
    }
    
}
