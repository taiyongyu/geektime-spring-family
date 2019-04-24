package geektime.spring.springbucks.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/**
 * 使用@RedisHash标注一个实体，使用的名字是springbucks-coffee，生存期是60秒
 */
@RedisHash(value = "springbucks-coffee", timeToLive = 60)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoffeeCache {
    /**
     * id作为Id
     */
    @Id
    private Long id;
    /**
     * name作为索引
     */
    @Indexed
    private String name;
    private Money price;
}
