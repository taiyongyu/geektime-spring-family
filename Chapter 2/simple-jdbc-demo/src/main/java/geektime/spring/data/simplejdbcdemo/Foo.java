package geektime.spring.data.simplejdbcdemo;

import lombok.Builder;
import lombok.Data;

/**
 * 使用lombok来帮助简化类定义，只需要给出类名和成员变量即可。需要使用@Data和@Builder注解
 *  @Data注解 ：注解在类上，提供类所有属性的 getting 和 setting 方法，此外还提供了equals、canEqual、hashCode、toString 方法
 *  @Builder注解：表示可以进行Builder方式初始化。这样更符合封装的原则，不对外公开属性的写操作。
 */

@Data
@Builder
public class Foo {
    private Long id;
    private String bar;
}
