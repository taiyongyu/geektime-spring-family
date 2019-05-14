package geektime.spring.springbucks.waiter;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import geektime.spring.springbucks.waiter.controller.PerformanceInteceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories
@EnableCaching
public class WaiterServiceApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(WaiterServiceApplication.class, args);
	}
    /**
	 * 实现WebMvcConfigurer配置类
	 * 重写addInterceptors()方法
     */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		/**
		 * 创建PerformanceInteceptor拦截器
		 * 拦截所有/coffee/下面的请求
		 * 拦截所有/order/下面的请求
		 */
		registry.addInterceptor(new PerformanceInteceptor())
				.addPathPatterns("/coffee/**").addPathPatterns("/order/**");
	}

	@Bean
	public Hibernate5Module hibernate5Module() {
		return new Hibernate5Module();
	}

	/**
	 * 这个方法的主要作用是美化json，缩进两格
	 * @return
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonBuilderCustomizer() {
		return builder -> {
			builder.indentOutput(true);
			/**
			 * 指定使用上海的时区
			 */
			builder.timeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		};
	}
}
