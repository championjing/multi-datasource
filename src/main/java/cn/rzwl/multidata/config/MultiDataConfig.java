package cn.rzwl.multidata.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

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
@Slf4j
public class MultiDataConfig {

    @Value("${spring.datasource.type}")
    private Class<? extends DataSource> dataSourceType;


    @Bean("test1")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.druid.test1")
    public DataSource test1(){
        log.info("构建test1数据源");
        DataSource source = DataSourceBuilder.create().type(dataSourceType).build();
        return source;
    }

    @Bean("test2")
    @ConfigurationProperties(prefix = "spring.datasource.druid.test2")
    public DataSource test2(){
        log.info("构建test2--- 数据源");
        DataSource source = DataSourceBuilder.create().type(dataSourceType).build();
        return source;
    }

    @Bean("routingDataSource")
    public AbstractRoutingDataSource routingDataSource(@Qualifier("test1") DataSource test1,@Qualifier("test2") DataSource test2){
        DynamicDataSourceRouter assistant = new DynamicDataSourceRouter();
        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put(DynamicDataSourceRouter.SourceName.TEST1.dbName,test1);
        dataSources.put(DynamicDataSourceRouter.SourceName.TEST2.dbName,test2);
        assistant.setDefaultTargetDataSource( test2 );
        assistant.setTargetDataSources(dataSources);
        return assistant;
    }
}
