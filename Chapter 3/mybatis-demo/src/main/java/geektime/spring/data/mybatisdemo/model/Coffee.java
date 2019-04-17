package geektime.spring.data.mybatisdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * 注意跟t_coffee表中各字段的对应关系。注意驼峰式。
 *
 */
public class Coffee {
    private Long id;
    private String name;
    private Money price;
    private Date createTime;
    private Date updateTime;
}
