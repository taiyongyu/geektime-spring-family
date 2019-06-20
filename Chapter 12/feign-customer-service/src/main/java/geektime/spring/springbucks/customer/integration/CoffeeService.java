package geektime.spring.springbucks.customer.integration;

import geektime.spring.springbucks.customer.model.Coffee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
/**
 * 使用@FeignClient 注解，来调用远程接口。 其中name指明远程接口的serviceId，contextId指明bean name，path指明url地址
 */
@FeignClient(name = "waiter-service", contextId = "coffee", path = "/coffee")
// 建议不要在接口上加@RequestMapping
public interface CoffeeService {
    @GetMapping(path = "/", params = "!name")
    List<Coffee> getAll();

    @GetMapping("/{id}")
    Coffee getById(@PathVariable("id") Long id);

    @GetMapping(path = "/", params = "name")
    Coffee getByName(@RequestParam("name") String name);
}
