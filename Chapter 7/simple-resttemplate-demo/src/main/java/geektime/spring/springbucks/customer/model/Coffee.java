package geektime.spring.springbucks.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coffee implements Serializable {
    private Long id;
    private String name;
    private BigDecimal price; // 先用BigDecimal，下次换Money
    private Date createTime;
    private Date updateTime;

    /**
     * 问题：涉及到金额时为什么不能用BigDecimal，而是建议用joda Money对象呢？
     * 答：因为货币复杂，专门还有个JSR 354来处理货币。不同的币种他们的单位不同，小数的位数也不同。
     *    以人民币为例，可以用元表示，也可以用分表示，后者数字是前者的100倍。
     *    如果换个币种呢，也许是10000倍，比如伊拉克第纳尔.
     *    金额要和货币绑定在一起。也许还会碰上货币转换等等工作，所以还是用专门的API比较方便。
     */
}
