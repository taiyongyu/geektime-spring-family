package geektime.spring.springbucks.customer;

import geektime.spring.springbucks.customer.model.Coffee;
import geektime.spring.springbucks.customer.support.CustomConnectionKeepAliveStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Slf4j
public class CustomerServiceApplication implements ApplicationRunner {
	@Autowired
	private RestTemplate restTemplate;

	public static void main(String[] args) {
		new SpringApplicationBuilder()
				.sources(CustomerServiceApplication.class)
				.bannerMode(Banner.Mode.OFF)
				.web(WebApplicationType.NONE)
				.run(args);
	}

	/**
	 * 调用REST API，会有很多选择。原始一点的JDK自带的，再进一步点使用HttpClient。
	 * 使用的SpringMVC，所以直接使用RestTemplate。
	 * 使用RestTemplate比直接使用Httpclient简单很多，同时也可以借助httpclient来实现RestTemplate。
	 * 通过使用RestTemplate仅仅只需要写几行代码，就可以完成直接使用httpclient很多行代码的事情。
	 *
	 * RestTemplate有三个构造函数：
	 * 1、默认构造函数，默认使用SimpleClientHttpRequestFactory，使用JDK自带的java.net包进行网络传输。
	 * 2、public RestTemplate(ClientHttpRequestFactory requestFactory)。
	 * 	传入一个ClientHttpRequestFactory。
	 * 	ClientHttpRequestFactory在Spring中的实现有很多个，
	 * 	如HttpComponentsClientHttpRequestFactory，Netty4ClientHttpRequestFactory等。
	 * 	HttpComponentsClientHttpRequestFactory，需要用到 HttpClient.
	 * 3、public RestTemplate(List<HttpMessageConverter<?>> messageConverters),
	 * 	使用SpringMvc的应该对HttpMessageConverter很熟悉，RestTemplate默认会给我们设置好常用的HttpMessageConverter。
	 */

	/**
	 * 构造自定义的requestFactory
	 * @return
	 */
	@Bean
	public HttpComponentsClientHttpRequestFactory requestFactory() {
		// 构造连接池的连接管理器，TTL(Time To Live)设置为30秒
		PoolingHttpClientConnectionManager connectionManager =
				new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);
		// 最大连接数为20
		connectionManager.setMaxTotal(200);
		// 每个router的最大连接数为20
		connectionManager.setDefaultMaxPerRoute(20);

        // 自定义的Http连接
		CloseableHttpClient httpClient = HttpClients.custom()
				.setConnectionManager(connectionManager)   // 使用自定义的连接管理器
				.evictIdleConnections(30, TimeUnit.SECONDS)
				.disableAutomaticRetries()  // 关闭自动重试
				// 有 Keep-Alive 认里面的值，没有的话永久有效
				//.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
				// 换成自定义的
				.setKeepAliveStrategy(new CustomConnectionKeepAliveStrategy())  // 设置keepalive策略
				.build();
        // 用自定义的httpClient来构造requestFactory
		HttpComponentsClientHttpRequestFactory requestFactory =
				new HttpComponentsClientHttpRequestFactory(httpClient);

		return requestFactory;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
//		return new RestTemplate();

		return builder
				.setConnectTimeout(Duration.ofMillis(100)) // 设置连接超时
				.setReadTimeout(Duration.ofMillis(500)) // 设置read超时
				.requestFactory(this::requestFactory) // 使用自定义的requestFactory进行构造
				.build();
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		URI uri = UriComponentsBuilder
				.fromUriString("http://localhost:8080/coffee/?name={name}")
				.build("mocha");
		RequestEntity<Void> req = RequestEntity.get(uri)
				.accept(MediaType.APPLICATION_XML)
				.build();
		ResponseEntity<String> resp = restTemplate.exchange(req, String.class);
		log.info("Response Status: {}, Response Headers: {}", resp.getStatusCode(), resp.getHeaders().toString());
		log.info("Coffee: {}", resp.getBody());

		String coffeeUri = "http://localhost:8080/coffee/";
		Coffee request = Coffee.builder()
				.name("Americano")
				.price(Money.of(CurrencyUnit.of("CNY"), 25.00))
				.build();
		Coffee response = restTemplate.postForObject(coffeeUri, request, Coffee.class);
		log.info("New Coffee: {}", response);

		ParameterizedTypeReference<List<Coffee>> ptr =
				new ParameterizedTypeReference<List<Coffee>>() {};
		ResponseEntity<List<Coffee>> list = restTemplate
				.exchange(coffeeUri, HttpMethod.GET, null, ptr);
		list.getBody().forEach(c -> log.info("Coffee: {}", c));
	}
}
