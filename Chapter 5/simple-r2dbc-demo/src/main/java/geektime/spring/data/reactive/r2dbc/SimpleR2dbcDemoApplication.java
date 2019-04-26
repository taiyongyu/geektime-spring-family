package geektime.spring.data.reactive.r2dbc;

import geektime.spring.data.reactive.r2dbc.converter.MoneyReadConverter;
import geektime.spring.data.reactive.r2dbc.converter.MoneyWriteConverter;
import geektime.spring.data.reactive.r2dbc.model.Coffee;
import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.dialect.Dialect;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.data.r2dbc.function.convert.R2dbcCustomConversions;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@Slf4j
/**
 * 这是一个Milestone的项目，springboot没有为它进行自动配置。
 * 因此需要先继承一下AbstractR2dbcConfiguration类，进行一些基本的配置。
 * 然后需要自己实现一些方法。
 */
public class SimpleR2dbcDemoApplication extends AbstractR2dbcConfiguration
		implements ApplicationRunner {
	@Autowired
	private DatabaseClient client;

	public static void main(String[] args) {
		SpringApplication.run(SimpleR2dbcDemoApplication.class, args);
	}

	/**
	 * 自定义实现connectionFactory
	 * 连接H2数据库
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
	 * 自定义Converter
	 */
	@Bean
	public R2dbcCustomConversions r2dbcCustomConversions() {
		Dialect dialect = getDialect(connectionFactory());
		CustomConversions.StoreConversions storeConversions =
				CustomConversions.StoreConversions.of(dialect.getSimpleTypeHolder());
		return new R2dbcCustomConversions(storeConversions,
				Arrays.asList(new MoneyReadConverter(), new MoneyWriteConverter()));
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		CountDownLatch cdl = new CountDownLatch(2);
        // reactive方式访问R2DBC
		// 两种方式
		client.execute()
				.sql("select * from t_coffee")
				.as(Coffee.class)
				.fetch()
				.first()
				.doFinally(s -> cdl.countDown())
//				.subscribeOn(Schedulers.elastic())
				.subscribe(c -> log.info("Fetch execute() {}", c));

		client.select()
				.from("t_coffee")
				.orderBy(Sort.by(Sort.Direction.DESC, "id"))
				.page(PageRequest.of(0, 3))
				.as(Coffee.class)
				.fetch()
				.all()
				.doFinally(s -> cdl.countDown())
//				.subscribeOn(Schedulers.elastic())
				.subscribe(c -> log.info("Fetch select() {}", c));

		log.info("After Starting.");
		cdl.await();
	}
}
