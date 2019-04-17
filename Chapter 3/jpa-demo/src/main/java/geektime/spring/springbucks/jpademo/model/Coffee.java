package geektime.spring.springbucks.jpademo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.joda.money.Money;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 标注这是一个实体
 * 对应数据库中的T_MENU表
 * 使用Lombok插件生成builder构造器、getter、setter、toString等方法以及一个不包含任何参数的构造方法和一个包含所有参数的构造方法
 *
 */
@Entity
@Table(name = "T_MENU")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coffee implements Serializable {
    /**
     * 声明id字段是主键，且自动生成。
     * 这是使用默认的生成策略，也可以指定其余的生成策略。
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 字段名与列名相同，则@Column注解可以省略
     */
    private String name;
    /**
     * 声明price字段是表中的一个列名。type指明joda包中的PersistentMoneyAmount类
     * 其中的currencyCode参数指明为CNY，代表使用人民币。
     * 通过源码分析可知，PersistentMoneyAmount类将一个Money类型的数据映射成了BigDecimal类型的字段。
     * 也可以使用PersistentMoneyMajorAmount类将Money类型数据映射成long型或bigInt型字段。
     * 同理，也有PersistentMoneyMinorAmount
     */
    /**
     * 金额不止是一个小数，它还有货币单位之类的东西，复杂的还有货币转换，各种汇率之类的问题。
     * 用Money类型可以明确地表示我这个属性是个货币，也更容易区分。
     */
    @Column
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money price;

    /**
     * createTime实体的创建时间，不可更改。
     * 使用 @CreationTimestamp注解标注，进行自动设置。
     */
    @Column(updatable = false)
    @CreationTimestamp
    private Date createTime;
    /**
     * updateTime实体的更新时间，默认可更改。
     * 使用@UpdateTimestamp注解标注，进行自动设置
     */
    @UpdateTimestamp
    private Date updateTime;
}
