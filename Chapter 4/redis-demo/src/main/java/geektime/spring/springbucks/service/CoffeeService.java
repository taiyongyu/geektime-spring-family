package geektime.spring.springbucks.service;

import geektime.spring.springbucks.model.Coffee;
import geektime.spring.springbucks.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Slf4j
@Service
public class CoffeeService {
    private static final String CACHE = "springbucks-coffee";
    @Autowired
    private CoffeeRepository coffeeRepository;
    /**
     * coffee的名字作为key，Coffee作为value 放入缓存中
     * 需要为redisTemplate指明key和value的类型
     */
    @Autowired
    private RedisTemplate<String, Coffee> redisTemplate;

    public List<Coffee> findAllCoffee() {
        return coffeeRepository.findAll();
    }

    public Optional<Coffee> findOneCoffee(String name) {
        // 第一个参数对应redis中的key，它的value是一个map。第二个参数是map中的key，第三个参数是map中的value
        // 取出redis中的所有hashmap对象
        HashOperations<String, String, Coffee> hashOperations = redisTemplate.opsForHash();
        // 判断redis和hashmap中是否存在相应的key
        // 如果存在，就直接返回
        if (redisTemplate.hasKey(CACHE) && hashOperations.hasKey(CACHE, name)) {
            log.info("Get coffee {} from Redis.", name);
            return Optional.of(hashOperations.get(CACHE, name));
        }
        // 如果不存在，到数据库中去查找再放入redis里面。
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", exact().ignoreCase());
        Optional<Coffee> coffee = coffeeRepository.findOne(
                Example.of(Coffee.builder().name(name).build(), matcher));
        log.info("Coffee Found: {}", coffee);
        if (coffee.isPresent()) {
            log.info("Put coffee {} to Redis.", name);
            // 将结果放入redis中
            hashOperations.put(CACHE, name, coffee.get());
            // 设置过期时间
            redisTemplate.expire(CACHE, 1, TimeUnit.MINUTES);
        }
        return coffee;
    }
}
