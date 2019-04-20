package geektime.spring.data.mongodemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 使用@Document注解标注文档
 */
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coffee {
    /**
     * 使用@Id标注id，注意import的包跟之前的是不一样的。
     */
    @Id
    private String id;
    private String name;
    private Money price;
    private Date createTime;
    private Date updateTime;
}
