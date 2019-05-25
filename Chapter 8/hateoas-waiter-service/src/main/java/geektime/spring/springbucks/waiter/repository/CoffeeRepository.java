package geektime.spring.springbucks.waiter.repository;

import geektime.spring.springbucks.waiter.model.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * 使用Spring Data Rest注解，指明path路径
 * 利用Spring Data JPA，自定义了两个查询方法
 */
@RepositoryRestResource(path = "/coffee")
public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
    List<Coffee> findByNameInOrderById(@Param(value = "list")List<String> list);
    Coffee findByName(@Param(value = "name") String name);

    /**
     * 注意，在源代码中findByName方法并没有@Param(value = "name")
     * 但是在实际运行中代码会报错，必须添加上@Param注解才可以
     */
}
