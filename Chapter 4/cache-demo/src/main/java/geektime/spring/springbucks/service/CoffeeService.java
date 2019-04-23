package geektime.spring.springbucks.service;

import geektime.spring.springbucks.model.Coffee;
import geektime.spring.springbucks.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Slf4j
@Service
@CacheConfig(cacheNames = "coffee")
/**
 * 在类名称上使用@CacheConfig配置缓存名，表示所有方法都共用这一个相同的缓存名
 */
public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;

    /**
     * 使用@Cacheable注解表示缓存该方法的返回值。
     * 后续对该方法的调用，可以不执行该方法，直接从缓存中返回结果。
     *
     * 在方法上定义会覆盖@CacheConfig的定义，如指定单独的缓存名称
     * @Cacheable("allCoffee") 表示findAllCoffee关联的缓存名称为allCoffee
     */
    @Cacheable
    public List<Coffee> findAllCoffee() {
        return coffeeRepository.findAll();
    }

    /**
     * @CacheEvict 用于从缓存中删除数据。
     */
    @CacheEvict
    public void reloadCoffee() {
    }

    public Optional<Coffee> findOneCoffee(String name) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", exact().ignoreCase());
        Optional<Coffee> coffee = coffeeRepository.findOne(
                Example.of(Coffee.builder().name(name).build(), matcher));
        log.info("Coffee Found: {}", coffee);
        return coffee;
    }
}
