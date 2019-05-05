package geektime.spring.springbucks;

import geektime.spring.springbucks.model.Coffee;
import geektime.spring.springbucks.model.CoffeeOrder;
import geektime.spring.springbucks.model.OrderState;
import geektime.spring.springbucks.service.CoffeeService;
import geektime.spring.springbucks.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Component
@Slf4j
/**
 * springboot提供了两个接口，分别为CommandLineRunner和ApplicationRunner。他们的执行时机为容器启动完成的时候。
 * 这两个接口中都有一个run方法，我们只需要实现这个方法即可。
 * 这两个接口的不同之处在于：ApplicationRunner中run方法的参数为ApplicationArguments，而CommandLineRunner接口中run方法的参数为String数组。
 * 想要更详细地获取命令行参数，那就使用ApplicationRunner接口。
 */
public class SpringbucksRunner implements ApplicationRunner {

    @Autowired
    private CoffeeService coffeeService;
    @Autowired
    private OrderService orderService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        coffeeService.initCache()
                .then(
                        coffeeService.findOneCoffee("mocha")
                                .flatMap(c -> {
                                    CoffeeOrder order = createOrder("Li Lei", c);
                                    return orderService.create(order);
                                })
                                .doOnError(t -> log.error("error", t)))
                .subscribe(o -> log.info("Create Order: {}", o));
        log.info("After Subscribe");
        Thread.sleep(5000);
    }

    private CoffeeOrder createOrder(String customer, Coffee... coffee) {
        return CoffeeOrder.builder()
                .customer(customer)
                .items(Arrays.asList(coffee))
                .state(OrderState.INIT)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
    }
}
