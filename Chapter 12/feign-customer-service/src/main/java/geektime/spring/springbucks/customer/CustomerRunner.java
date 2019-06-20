package geektime.spring.springbucks.customer;

import geektime.spring.springbucks.customer.integration.CoffeeOrderService;
import geektime.spring.springbucks.customer.integration.CoffeeService;
import geektime.spring.springbucks.customer.model.Coffee;
import geektime.spring.springbucks.customer.model.CoffeeOrder;
import geektime.spring.springbucks.customer.model.NewOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class CustomerRunner implements ApplicationRunner {
    /**
     * 有了Feign的支持之后，这里的调用就变得很简单了
     * 这里调用远程的服务接口，就变得像调用本地service一样。
     * 不用再写resttemplate或者httpclient等。
     */
    @Autowired
    private CoffeeService coffeeService;
    @Autowired
    private CoffeeOrderService coffeeOrderService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        readMenu();
        Long id = orderCoffee();
        queryOrder(id);
    }

    private void readMenu() {
        List<Coffee> coffees = coffeeService.getAll();
        coffees.forEach(c -> log.info("Coffee: {}", c));
    }

    private Long orderCoffee() {
        NewOrderRequest orderRequest = NewOrderRequest.builder()
                .customer("Li Lei")
                .items(Arrays.asList("capuccino"))
                .build();
        CoffeeOrder order = coffeeOrderService.create(orderRequest);
        log.info("Order ID: {}", order.getId());
        return order.getId();
    }

    private void queryOrder(Long id) {
        CoffeeOrder order = coffeeOrderService.getOrder(id);
        log.info("Order: {}", order);
    }
}
