package geektime.spring.springbucks.customer;

import geektime.spring.springbucks.customer.support.CustomConnectionKeepAliveStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
@EnableFeignClients
/**
 * @EnableDiscoveryClient 注解用于服务发现
 * @EnableFeignClients 注解用于开启Feign客户端
 */
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	/**
	 * 自定义了httpClient，使用Feign之后，虽然在程序中不需要显式的调用httpClient
	 * 但是在底层还是需要用到。因此在这里做一个个性化定制。
	 * @return
	 */
	@Bean
	public CloseableHttpClient httpClient() {
		return HttpClients.custom()
				.setConnectionTimeToLive(30, TimeUnit.SECONDS)
				.evictIdleConnections(30, TimeUnit.SECONDS)
				.setMaxConnTotal(200)
				.setMaxConnPerRoute(20)
				.disableAutomaticRetries()
				.setKeepAliveStrategy(new CustomConnectionKeepAliveStrategy())
				.build();
	}
}
