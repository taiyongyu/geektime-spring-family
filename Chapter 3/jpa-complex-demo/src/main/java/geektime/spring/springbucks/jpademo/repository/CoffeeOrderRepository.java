package geektime.spring.springbucks.jpademo.repository;

import geektime.spring.springbucks.jpademo.model.CoffeeOrder;

import java.util.List;

/**
 * 继承BaseRepository类，不需要@Repository注解
 * 在类中定义自己需要的接口即可，不需要手动实现。
 */
public interface CoffeeOrderRepository extends BaseRepository<CoffeeOrder, Long> {
    List<CoffeeOrder> findByCustomerOrderById(String customer);

    /**
     * 通过_告诉Spring Data，要访问的是item里的name属性，避免混淆。
     */
    List<CoffeeOrder> findByItems_Name(String name);
}
