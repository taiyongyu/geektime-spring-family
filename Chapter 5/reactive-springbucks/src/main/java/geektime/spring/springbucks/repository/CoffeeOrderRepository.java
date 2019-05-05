package geektime.spring.springbucks.repository;

import geektime.spring.springbucks.model.CoffeeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
/**
 * 使用@Repository注解的方式，可以手写比较复杂的sql逻辑
 */
@Repository
public class CoffeeOrderRepository {
    /**
     * R2DBC操作数据库内置对象
     */
    @Autowired
    private DatabaseClient databaseClient;

    public Mono<Long> save(CoffeeOrder order) {
        // 往两张表里同时插入数据
        // 先插入一张表，取出id，再插入另一张表
        return databaseClient.insert().into("t_order")
                .value("customer", order.getCustomer())
                .value("state", order.getState().ordinal())
                .value("create_time", new Timestamp(order.getCreateTime().getTime()))
                .value("update_time", new Timestamp(order.getUpdateTime().getTime()))
                .fetch()
                .first()
                .flatMap(m -> Mono.just((Long) m.get("ID")))
                .flatMap(id -> Flux.fromIterable(order.getItems())
                        .flatMap(c -> databaseClient.insert().into("t_order_coffee")
                                .value("coffee_order_id", id)
                                .value("items_id", c.getId())
                                .then()).then(Mono.just(id)));
    }
}
