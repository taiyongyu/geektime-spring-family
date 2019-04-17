package geektime.spring.springbucks.jpademo.repository;

import geektime.spring.springbucks.jpademo.model.CoffeeOrder;
import org.springframework.data.repository.CrudRepository;

public interface CoffeeOrderRepository extends CrudRepository<CoffeeOrder, Long> {
    /**
     * 在CrudRepository中指明要处理的实体类和ID的类型
     * 查看CrudRepository的源码，它继承了Repository类。
     * 里面已经封装好了许多接口，如:
     * save
     * saveAll
     * findById
     * findAll
     * findAllById
     * count
     * delete等等
     */
}
