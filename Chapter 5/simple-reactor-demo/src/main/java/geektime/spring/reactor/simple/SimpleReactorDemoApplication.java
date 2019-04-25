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
				.doOnNext( i -> log.info("current emit data {} ", i))
				.doOnRequest(n -> log.info("Request {} number", n))
				.subscribe(i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
						e -> log.error("error {}", e.toString()),
						() -> log.info("Subscriber COMPLETE") //,
//						s -> s.request(4)
				);
	}
}

