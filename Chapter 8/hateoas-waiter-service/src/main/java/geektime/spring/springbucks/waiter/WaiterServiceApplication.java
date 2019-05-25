package geektime.spring.springbucks.waiter;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.TimeZone;

@SpringBootApplication
@EnableCaching
public class WaiterServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaiterServiceApplication.class, args);
	}

	/**
	 *  注册Hibernate5的支持
	 * @return
	 */
	@Bean
	public Hibernate5Module hibernate5Module() {
		return new Hibernate5Module();
	}

	/**
	 * 对JacksonBuilder做定制
	 * 1、返回的json进行了格式化，带有缩进
	 * 2、指定时区
	 * @return
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonBuilderCustomizer() {
		return builder -> {
			builder.indentOutput(true);
			builder.timeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		};
	}

	/**
	 * 启动程序后，
	 */
}
