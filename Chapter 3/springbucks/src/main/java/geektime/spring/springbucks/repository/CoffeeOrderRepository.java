package geektime.spring.springbucks.repository;

import geektime.spring.springbucks.model.CoffeeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 由于这里使用jap的方式实现某个接口，因此不需要@Repository注解
 */
public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder, Long> {
}
