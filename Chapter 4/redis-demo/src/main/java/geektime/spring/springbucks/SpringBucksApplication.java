package geektime.spring.springbucks;

import geektime.spring.springbucks.model.Coffee;
import geektime.spring.springbucks.service.CoffeeService;
import io.lettuce.core.ReadFrom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.UnknownHostException;
import java.util.Optional;

@Slf4j
@EnableTransactionManagement
@SpringBootApplication
@EnableJpaRepositories
public class SpringBucksApplication implements ApplicationRunner {
	@Autowired
	private CoffeeService coffeeService;

	public static void main(String[] args) {
		SpringApplication.run(SpringBucksApplication.class, args);
	}

	/**
	 * spring 中的redisTemplate默认是redisTemplate<Object>的，不符合实际需求
	 * 因此，要手动声明一个符合实际类型的RedisTemplate<String, Coffee>
	 */
	@Bean
	public RedisTemplate<String, Coffee> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Coffee> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	/**
	 * 利用回调配置类，配置从主节点读取数据。(此配置针对redis集群，本例中只是单点模式，这里只是做一个演示了解即可)
	 */
	@Bean
	public LettuceClientConfigurationBuilderCustomizer customizer() {

		return builder -> builder.readFrom(ReadFrom.MASTER_PREFERRED);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 第一次是从数据库中查询的，然后放入redis中
		Optional<Coffee> c = coffeeService.findOneCoffee("mocha");
		log.info("Coffee {}", c);
        // 直接从redis中获取数据
		for (int i = 0; i < 5; i++) {
			c = coffeeService.findOneCoffee("mocha");
		}
		log.info("Value from Redis: {}", c);
	}
}

