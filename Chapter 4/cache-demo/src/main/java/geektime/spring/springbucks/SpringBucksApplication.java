package geektime.spring.springbucks;

import geektime.spring.springbucks.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableTransactionManagement
@SpringBootApplication
@EnableJpaRepositories
@EnableCaching(proxyTargetClass = true)
/**
 * 使用@EnableCaching注解开启缓存。
 * 其中proxyTargetClass=true表示：
 * 	当需要代理的类是一个接口或者是一个动态生成的代理类时使用JdkProxy代理；
 * 	而当要代理的类是一个具体类时，使用cglib来代理。
 * 	假如不设置该属性，则默认使用JdkProxy代理，而JdkProxy能够代理的类必须实现接口，
 * 	因此如果想要一个没实现接口的类被代理，就必须设置proxyTargetClass = true来使用cglib完成代理。
 */
/**
 * 在这个例子中，没有配置redis等分布式缓存。那么本例默认就将数据缓存到JVM中。
 */
public class SpringBucksApplication implements ApplicationRunner {
	@Autowired
	private CoffeeService coffeeService;

	public static void main(String[] args) {
		SpringApplication.run(SpringBucksApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 第一次会从数据库中加载记录，然后被缓存
		log.info("Count: {}", coffeeService.findAllCoffee().size());

		for (int i = 0; i < 10; i++) {
			log.info("Reading from cache.");
			// 已经被缓存过了，那么就将从缓存中加载数据
			coffeeService.findAllCoffee();
		}
		// 清除缓存
		coffeeService.reloadCoffee();
		log.info("Reading after refresh.");
		// 重新从数据库中加载记录
		coffeeService.findAllCoffee().forEach(c -> log.info("Coffee {}", c.getName()));
	}
}

