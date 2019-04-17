package geektime.spring.springbucks.jpademo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 使用了父类之后，声明继承该父类
 * 注意，要声明一下@ToString(callSuper = true)继承父类中的字段
 * 否则，本实体中的toString方法只能包含本实体内的字段。
 */
@Entity
@Table(name = "T_MENU")
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Coffee extends BaseEntity implements Serializable {
    private String name;
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money price;
}

/**
 * 共享相关资料如下：
 * 　　子类继承基类后，使用＠Data注解会有编辑器底色警告，告知你在生成hashcode等过程中，基类的内容不会被生成。此时，要添加@EqualsAndHashCode(callSuper=true)即可手动标记子类hash时要调用父类的hash方法对属于父类的部分内容生成哈希值。此时就不会报警告了。（包括下面那句@ToString(callSuper = true)也是一样）
 * 　　如果觉得此方式比较麻烦的话（每个类都要），可设置lombok的配置文件lombok.config来解决：
 * 　　①lombok.config文件需要放在src/main/java文件夹下的目录中（也可以放在实体同级目录下），其它位置无效。内容如下：
 * config.stopBubbling=true
 * lombok.equalsAndHashCode.callSuper=call
 * 　　②然后，在pom中加入插件：
 * <plugin>
 *    <groupId>org.apache.maven.plugins</groupId>
 *    <artifactId>maven-compiler-plugin</artifactId>
 *    <configuration>
 *       <source>1.8</source>
 *       <target>1.8</target>
 *    </configuration>
 * </plugin>
 * 　　此时，可见@Data编辑器警告底色消失。
 */
