package geektime.spring.data.reactive.redisdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@Slf4j
public class RedisDemoApplication implements ApplicationRunner {
    private static final String KEY = "COFFEE_MENU";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(RedisDemoApplication.class, args);
    }

    /**
     * 自定义一个reactiveRedisTemplate对象，它的kay和value，都是String类型的
     * @param factory
     * @return
     */
    @Bean
    ReactiveStringRedisTemplate reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        return new ReactiveStringRedisTemplate(factory);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 取出redis中的所有hashmap对象
        ReactiveHashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        CountDownLatch cdl = new CountDownLatch(1);
        // 在数据库中查询，构建Coffee列表
        List<Coffee> list = jdbcTemplate.query(
                "select * from t_coffee", (rs, i) ->
                Coffee.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .price(rs.getLong("price"))
                        .build()
        );

        Flux.fromIterable(list)// 构建Flux对象
                .publishOn(Schedulers.single())// 开启一个single线程
                .doOnComplete(() -> log.info("list ok"))
                .flatMap(c -> {   // 使用flatMap处理每个元素
                    log.info("try to put {},{}", c.getName(), c.getPrice());
                    return hashOps.put(KEY, c.getName(), c.getPrice().toString());
                })
                .doOnComplete(() -> log.info("set ok"))
                .concatWith(redisTemplate.expire(KEY, Duration.ofMinutes(1)))  // 设置超时时间，为1分钟。
                .doOnComplete(() -> log.info("expire ok"))
                .onErrorResume(e -> {   // 异常处理
                    log.error("exception {}", e.getMessage());
                    return Mono.just(false);
                })
                .subscribe(b -> log.info("Boolean: {}", b),
                        e -> log.error("Exception {}", e.getMessage()),
                        () -> cdl.countDown());
        log.info("Waiting");
        // 因为是在一个singe线程中执行，等待所有操作结束之后再退出。
        cdl.await();
    }
}
