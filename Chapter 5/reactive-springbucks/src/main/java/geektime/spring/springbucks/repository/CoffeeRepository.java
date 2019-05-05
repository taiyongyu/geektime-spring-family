package geektime.spring.springbucks.repository;

import geektime.spring.springbucks.model.Coffee;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.query.Query;
import reactor.core.publisher.Mono;

/**
 * 使用继承Repository接口的方式，操作数据库
 * 利用@Query注解和父接口的方法，能处理简单的SQL。
 */

public interface CoffeeRepository extends R2dbcRepository<Coffee, Long> {
    @Query("select * from t_coffee where name=$1")
    Mono<Coffee> findByName(String name);
}
