package geektime.spring.springbucks.waiter.service;

import geektime.spring.springbucks.waiter.model.Coffee;
import geektime.spring.springbucks.waiter.model.CoffeeOrder;
import geektime.spring.springbucks.waiter.model.OrderState;
import geektime.spring.springbucks.waiter.repository.CoffeeOrderRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 自定义度量指标
 * 实现MeterBinder接口
 */
@Service
@Transactional
@Slf4j
public class CoffeeOrderService implements MeterBinder {
    @Autowired
    private CoffeeOrderRepository orderRepository;

    private Counter orderCounter = null;

    public CoffeeOrder get(Long id) {
        return orderRepository.getOne(id);
    }

    public CoffeeOrder createOrder(String customer, Coffee...coffee) {
        CoffeeOrder order = CoffeeOrder.builder()
                .customer(customer)
                .items(new ArrayList<>(Arrays.asList(coffee)))
                .state(OrderState.INIT)
                .build();
        CoffeeOrder saved = orderRepository.save(order);
        log.info("New Order: {}", saved);

        //每次创建订单的时候，计数器都+1
        orderCounter.increment();

        return saved;
    }

    public boolean updateState(CoffeeOrder order, OrderState state) {
        if (state.compareTo(order.getState()) <= 0) {
            log.warn("Wrong State order: {}, {}", state, order.getState());
            return false;
        }
        order.setState(state);
        orderRepository.save(order);
        log.info("Updated Order: {}", order);
        return true;
    }

    /**
     * 重写bindTo接口
     * @param meterRegistry
     */
    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        // 注册了一个名叫order.count的counter(计数器)
        this.orderCounter = meterRegistry.counter("order.count");
        // 在浏览器访问 http://localhost:8080/actuator/metrics/order.count 即可查看该计数器的变化
        // 也可以利用prometheus来查看该计数器：
        // 访问：http://localhost:8080/actuator/prometheus 找到order_count_total即可。这是prometheus自动做了名称的转换

        // 访问http://localhost:8080/actuator/metrics 可以查看其余的指标，例如访问次数的统计等等。
    }
}
