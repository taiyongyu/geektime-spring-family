package geektime.spring.reactor.webclient;

import geektime.spring.reactor.webclient.model.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@Slf4j
public class WebclientDemoApplication implements ApplicationRunner {
	@Autowired
	private WebClient webClient;

	public static void main(String[] args) {
		new SpringApplicationBuilder(WebclientDemoApplication.class)
				.web(WebApplicationType.NONE)
				.bannerMode(Banner.Mode.OFF)
				.run(args);
	}

	/**
	 * 使用WebClient.Builder的方式构造webClient，并指明url
	 * @param builder
	 * @return
	 */
	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		return builder.baseUrl("http://localhost:8080").build();
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		CountDownLatch cdl = new CountDownLatch(2);
        // 构造get请求
		webClient.get()
				.uri("/coffee/{id}", 1)
				.accept(MediaType.APPLICATION_JSON_UTF8) // 构造accept头信息，json utf8 格式
				.retrieve()// 获取response
				.bodyToMono(Coffee.class) // 转换成Coffee类型
				.doOnError(t -> log.error("Error: ", t))
				.doFinally(s -> cdl.countDown())
				.subscribeOn(Schedulers.single()) // 在singe线程中做subscribe
				.subscribe(c -> log.info("Coffee 1: {}", c));

		Mono<Coffee> americano = Mono.just(
				Coffee.builder()
						.name("americano")
						.price(Money.of(CurrencyUnit.of("CNY"), 25.00))
						.build()
		);
		// 构造post请求
		webClient.post()
				.uri("/coffee/")
				.body(americano, Coffee.class)// post的正文就是上面构造的 americano coffee
				.retrieve()// 获取结果
				.bodyToMono(Coffee.class)// 结果类型转换
				.doFinally(s -> cdl.countDown())
				.subscribeOn(Schedulers.single())
				.subscribe(c -> log.info("Coffee Created: {}", c));

		cdl.await();
        // await等待上面执行完毕
		webClient.get()
				.uri("/coffee/")
				.retrieve()
				.bodyToFlux(Coffee.class)// Flux对应多个结果(就是List)，Mono只对应一个结果
				.toStream()// 将List转换成Stream才能使用各种map、filter、forEach等方法
				.forEach(c -> log.info("Coffee in List: {}", c));
	}
}
