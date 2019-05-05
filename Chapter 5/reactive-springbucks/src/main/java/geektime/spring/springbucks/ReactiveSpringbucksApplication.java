package geektime.spring.springbucks;

import geektime.spring.springbucks.converter.MoneyReadConverter;
import geektime.spring.springbucks.converter.MoneyWriteConverter;
import geektime.spring.springbucks.model.Coffee;
import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.dialect.Dialect;
import org.springframework.data.r2dbc.function.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;

/**
 * 开启R2dbc的配置
 * 继承AbstractR2dbcConfiguration，进行手动配置
 */
@SpringBootApplication
@EnableR2dbcRepositories
@Slf4j
public class ReactiveSpringbucksApplication extends AbstractR2dbcConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveSpringbucksApplication.class, args);
    }

    /**
     * 手动配置connectionFactory
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        return new H2ConnectionFactory(
                H2ConnectionConfiguration.builder()
                        .inMemory("testdb")
                        .username("sa")
                        .build());
    }
    /**
     * 手动配置CustomConversions
     * @return
     */
    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        Dialect dialect = getDialect(connectionFactory());
        CustomConversions.StoreConversions storeConversions =
                CustomConversions.StoreConversions.of(dialect.getSimpleTypeHolder());
        return new R2dbcCustomConversions(storeConversions,
                Arrays.asList(new MoneyReadConverter(), new MoneyWriteConverter()));
    }

    /**
     * 覆盖spring boot 提供的默认的reactiveRedisTemplate
     * 对序列化方式进行订制。
     * key使用string的序列化方式
     * value使用Jackson2Json的序列化方式。
     * 默认的redisTemplate使用JdkSerializationRedisSerializer序列化方式，会产生各种\x0\x0\x00\x000\x0\x0
     * 用Jackson2JsonRedisSerializer，被序列化的类不需要实现Serializable接口，也不会出现使用spring-boot-devtools时反序列化遇到的类型转换异常。
     * @param factory
     * @return
     */
    @Bean
    public ReactiveRedisTemplate<String, Coffee> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Coffee> valueSerializer = new Jackson2JsonRedisSerializer<>(Coffee.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Coffee> builder
                = RedisSerializationContext.newSerializationContext(keySerializer);

        RedisSerializationContext<String, Coffee> context = builder.value(valueSerializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
