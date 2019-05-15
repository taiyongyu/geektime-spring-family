package geektime.spring.springbucks.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coffee implements Serializable {
    private Long id;
    private String name;
    // 这里使用Money类型做金额，那么在传输的时候要进行序列化与反序列化操作
    private Money price;
    private Date createTime;
    private Date updateTime;
}
