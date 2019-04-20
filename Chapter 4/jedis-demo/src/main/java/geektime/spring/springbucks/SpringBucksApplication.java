package geektime.spring.springbucks;

import geektime.spring.springbucks.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

@Slf4j
@EnableTransactionManagement
@SpringBootApplication
@EnableJpaRepositories
/**
 * 注意这里使用的是ApplicationRunner。 跟CommandLineRunner的区别后面会讲到
 */
public class SpringBucksApplication implements ApplicationRunner {
	@Autowired
	private CoffeeService coffeeService;
	@Autowired
	private JedisPool jedisPool;
	@Autowired
	private JedisPoolConfig jedisPoolConfig;

	public static void main(String[] args) {
		SpringApplication.run(SpringBucksApplication.class, args);
	}

	/**
	 * 读取配置文件中的redis属性，生成一个JedisPoolConfig
	 * @return
	 */
	@Bean
	@ConfigurationProperties("redis")
	public JedisPoolConfig jedisPoolConfig() {
		return new JedisPoolConfig();
	}

	/**
	 * 指明Bean销毁时，自动触发jedispool的close方法
	 * @param host
	 * @return
	 */
	@Bean(destroyMethod = "close")
	public JedisPool jedisPool(@Value("${redis.host}") String host) {
		return new JedisPool(jedisPoolConfig(), host);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 查看配置的连接池信息
		log.info(jedisPoolConfig.toString());
        // 获取到jedis实例后，不用手动去close或者释放。 由spring自己来控制。
		try (Jedis jedis = jedisPool.getResource()) {
			// 了解jedis的相关操作
			coffeeService.findAllCoffee().forEach(c -> {
				jedis.hset("springbucks-menu",
						c.getName(),
						Long.toString(c.getPrice().getAmountMinorLong()));
			});

			Map<String, String> menu = jedis.hgetAll("springbucks-menu");
			log.info("Menu: {}", menu);

			String price = jedis.hget("springbucks-menu", "espresso");
			log.info("espresso - {}",
					Money.ofMinor(CurrencyUnit.of("CNY"), Long.parseLong(price)));
		}
	}
}

