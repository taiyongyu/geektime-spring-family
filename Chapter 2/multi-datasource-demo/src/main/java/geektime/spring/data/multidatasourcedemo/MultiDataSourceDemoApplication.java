package geektime.spring.data.multidatasourcedemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 多数据源配置，在@SpringBootApplication中exclude掉自动配置的三个相关类
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class})
@Slf4j
public class MultiDataSourceDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiDataSourceDemoApplication.class, args);
    }

    /**
     * 通过@ConfigurationProperties()注解绑定application.properties中的属性，将属性映射到实体bean中
     *
     */
    @Bean
    @ConfigurationProperties("source.datasource")
    public DataSourceProperties sourceDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * @Bean是一个方法级别上的注解，主要用在@Configuration注解的类里，也可以用在@Component注解的类里。
     * 添加的bean的id即为方法名，方法的返回值即当作一个bean
     * 添加一个bean，bean的id即为sourceDataSource，可通过actuator/beans来查看，定义之后就可以通过@Autowire自动装配
     */
    @Bean
    public DataSource sourceDataSource() {
        DataSourceProperties dataSourceProperties = sourceDataSourceProperties();
        log.info("source datasource: {}", dataSourceProperties.getUrl());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }


    /**
     * @Resource注解加在方法上面，说明该方法的参数要按照名字(byName)的方式来自动注入已经存在的一个bean
     * 为sourceDataSource bean添加事务处理
     */
    @Bean
    @Resource
    public PlatformTransactionManager sourceTxManager(DataSource sourceDataSource) {
        return new DataSourceTransactionManager(sourceDataSource);
    }



    @Bean
    @ConfigurationProperties("target.datasource")
    public DataSourceProperties targetDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource targetDataSource() {
        DataSourceProperties dataSourceProperties = targetDataSourceProperties();
        log.info("target datasource: {}", dataSourceProperties.getUrl());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    @Resource
    public PlatformTransactionManager targetTxManager(DataSource targetDataSource) {
        return new DataSourceTransactionManager(targetDataSource);
    }

    /**
     * 这种多数据源配置的方式有一个缺点，就是在配置文件中添加一个数据源，就要在这里添加代码，不能达到动态配置数据源的效果。
     */
}

