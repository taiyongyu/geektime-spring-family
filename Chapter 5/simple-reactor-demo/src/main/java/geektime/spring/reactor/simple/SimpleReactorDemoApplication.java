package geektime.spring.reactor.simple;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
@Slf4j
public class SimpleReactorDemoApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(SimpleReactorDemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Flux.range(5, 6)
				.doOnComplete(() -> log.info("Publisher COMPLETE 1"))
				.doOnRequest(n -> log.info("after ：Request {} number", n))
				.publishOn(Schedulers.elastic())
				.map(i -> {
					log.info("Publish {}, {}", Thread.currentThread(), i);
					return 10 / (i - 8);
				})
				.onErrorResume(e -> {
					log.error("Exception {}", e.toString());
					return Mono.just(-1);
				})
				.doOnRequest(n -> log.info("before ：Request {} number", n))
				.doOnComplete(() -> log.info("Publisher COMPLETE 2"))
				.subscribeOn(Schedulers.single())
				.subscribe(i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
						e -> log.error("error {}", e.toString()),
						() -> log.info("Subscriber COMPLETE")
				);
		Thread.sleep(2000);
	}
}

