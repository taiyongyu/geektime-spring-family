package geektime.spring.springbucks.customer.integration;

import geektime.spring.springbucks.customer.model.Coffee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 演示熔断方法二：
 *  使用Feign调用远程方法，当出现问题时，调用fallback指定的类来进行处理。
 */
@FeignClient(name = "waiter-service", contextId = "coffee",
        qualifier = "coffeeService", path="/coffee",
        fallback = FallbackCoffeeService.class)
// 如果用了Fallback，不要在接口上加@RequestMapping，path可以用在这里
public interface CoffeeService {
    @GetMapping(path = "/", params = "!name")
    List<Coffee> getAll();

    @GetMapping("/{id}")
    Coffee getById(@PathVariable Long id);

    @GetMapping(path = "/", params = "name")
    Coffee getByName(@RequestParam String name);
}
