# multi-datasource
multi datasource dynamic switch
 
# 动态切换数据源

## 场景

- 大型应用中的读写分离；
- 相同业务逻辑，但是对应多个数据库；

## 实现方案
- 步骤
1. 核心是实现Spring 内置的 AbstractRoutingDataSource 抽象类，实现determineCurrentLookupKey() 方法；
2. 实例化出多个datasource，并将这些datasource加入到该抽象类中的targetDataSources。
3. 在操作数据前，dao层会先调用 AbstractRoutingDataSource 抽象类getConnection(),其中的调用的是determineCurrentLookupKey()，获取到对应的datasource；
4. 借助ThreadLocal保存数据源信息，在整个业务流程中方便获取;

- 难点
不同连接池实例化事务管理、和生成类似数据库的sessionFactory方法有所不同，可能会费些时间。

## 代码实现

### 环境介绍

- springboot版本： 1.5.6.RELEASE
- 连接池：alibaba druid

### 部分代码
- 配置文件, 密码就暴漏吧，哈哈
```
spring.datasource.druid.test1.url=jdbc:mysql://localhost:3306/multi_test1?characterEncoding=utf8&characterSetResults=utf8&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT
spring.datasource.druid.test1.username=root
spring.datasource.druid.test1.password=123456

spring.datasource.druid.test2.url=jdbc:mysql://localhost:3306/multi_test2?characterEncoding=utf8&characterSetResults=utf8&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT
spring.datasource.druid.test2.username=root
spring.datasource.druid.test2.password=123456

spring.datasource.type = com.alibaba.druid.pool.DruidDataSource

spring.jpa.show-sql=true
spring.jpa.hibernate.naming.physical-strategy=org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy

```
- 实现AbstractRoutingDataSource 抽象类

```
public class DynamicDataSourceRouter extends AbstractRoutingDataSource {
    
    public static final ThreadLocal<String> dbStore = new ThreadLocal<>();

    @Override
    protected Object determineCurrentLookupKey() {
        String dbBean = dbStore.get();
        if(StringUtils.isBlank(dbBean)){
            dbBean = SourceName.TEST1.dbName;
        } else {
            
        }
        return dbBean;
    }
    enum SourceName{
        TEST1("test1"),TEST2("test2");
        
        public String dbName;
        
        private SourceName(String name){
            this.dbName = name;
        }
    }
}
```
- 实例化出多个数据源，加入到DynamicDataSourceRouter父类的setTargetDataSources，并设置默认值

```
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
```
- 手动对 Jpa 的 EntityManager 进行初始化和配置

```
 */
@Configuration
@EnableConfigurationProperties(JpaProperties.class)
@EnableJpaRepositories(value = "cn.rzwl.multidata.dao")
public class JpaEntityManager {
    
    @Autowired
    private JpaProperties jpaProperties;

    @Resource(name = "routingDataSource")
    private DataSource routingDataSource;

    @Bean(name = "entityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
        // 不明白为什么这里获取不到 application.yml 里的配置
        Map<String, String> properties = jpaProperties.getProperties();
        //要设置这个属性，实现 CamelCase -> UnderScore 的转换
        properties.put("hibernate.physical_naming_strategy",
                "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        return builder
                .dataSource(routingDataSource)//关键：注入routingDataSource
                .properties(properties)
                .packages("cn.rzwl.multidata.entity") //TODO 改成自己的实体类包
                .persistenceUnit("myPersistenceUnit")
                .build();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory(EntityManagerFactoryBuilder builder) {
        return this.entityManagerFactoryBean(builder).getObject();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactory(builder));
    }
}
```
- 为方便测试，用filter验证

```
@WebFilter(urlPatterns = "/*")
@Slf4j
public class MyFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("初始化自定义过滤器");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String site = request.getParameter("source");
        if( DynamicDataSourceRouter.SourceName.TEST1.dbName.equals( site ) ){
            DynamicDataSourceRouter.dbStore.set( DynamicDataSourceRouter.SourceName.TEST1.dbName );
        } else {
            DynamicDataSourceRouter.dbStore.set( DynamicDataSourceRouter.SourceName.TEST2.dbName );
        }
        log.info("进入业务流程");
        chain.doFilter( request,response );
        log.info("完成处理");
        System.out.println("后续操作");
    }

    @Override
    public void destroy() {

    }
}

```
### 源码地址:
 
## 小结：

- 一个神奇的AbstractRoutingDataSource类，使数据库切换操作举重若轻，优雅得体，告别劳力式编程。