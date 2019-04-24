package geektime.spring.springbucks.repository;

import geektime.spring.springbucks.model.CoffeeCache;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * 定义repository，继承自CrudRepository
 */
public interface CoffeeCacheRepository extends CrudRepository<CoffeeCache, Long> {
    // 根据名字，查找一个coffeecache对象
    Optional<CoffeeCache> findOneByName(String name);
}
