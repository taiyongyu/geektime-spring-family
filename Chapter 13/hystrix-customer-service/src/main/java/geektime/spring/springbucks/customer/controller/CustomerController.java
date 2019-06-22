package geektime.spring.springbucks.customer.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import geektime.spring.springbucks.customer.integration.CoffeeOrderService;
import geektime.spring.springbucks.customer.integration.CoffeeService;
import geektime.spring.springbucks.customer.model.Coffee;
import geektime.spring.springbucks.customer.model.CoffeeOrder;
import geektime.spring.springbucks.customer.model.NewOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/customer")
@Slf4j
public class CustomerController {
    @Autowired
    private CoffeeService coffeeService;
    @Autowired
    private CoffeeOrderService coffeeOrderService;

    @GetMapping("/menu")
    public List<Coffee> readMenu() {
        List<Coffee> list = coffeeService.getAll();
        log.info("Read Menu: {} coffee", list.size());
        return list;
    }

    /**
     * 演示熔断方法一：
     *  使用@HystrixCommand注解，如果该方法调用出现问题
     *  那么跳转到fallbackMethod指定的方法中去执行。
     * @return
     */
    @PostMapping("/order")
    @HystrixCommand(fallbackMethod = "fallbackCreateOrder")
    public CoffeeOrder createOrder() {
        NewOrderRequest orderRequest = NewOrderRequest.builder()
                .customer("Li Lei")
                .items(Arrays.asList("capuccino"))
                .build();
        CoffeeOrder order = coffeeOrderService.create(orderRequest);
        log.info("Order ID: {}", order != null ? order.getId() : "-");
        return order;
    }

    public CoffeeOrder fallbackCreateOrder() {
        log.warn("Fallback to NULL order.");
        return null;
    }
}
