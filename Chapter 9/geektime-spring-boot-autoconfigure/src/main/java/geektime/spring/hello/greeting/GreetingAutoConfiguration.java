package geektime.spring.hello.greeting;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(GreetingApplicationRunner.class)
/**
 * 使用@Configuration注解声明这是一个配置类，应用启动的时候自动加载该配置类。
 * @ConditionalOnClass(GreetingApplicationRunner.class) 注解
 * 说明只有在classpath中有GreetingApplicationRunner这个类时配置才会生效。

 */
public class GreetingAutoConfiguration {
    /**
     * 在配置类中生成一个bean，生成这个bean有两个限制条件，必须同时满足。
     * (1)Spring上下文中不存在GreetingApplicationRunner这个类型的bean
     * (2)必须配置一个name为greeting.enabled的属性，它的值为true
     * 这个bean才会被创建。
     * matchIfMissing = true 指明如果没有配置这个属性，那么会默认自动配置上，且值为true
     */
    @Bean
    @ConditionalOnMissingBean(GreetingApplicationRunner.class)
    @ConditionalOnProperty(name = "greeting.enabled", havingValue = "true", matchIfMissing = true)
    public GreetingApplicationRunner greetingApplicationRunner() {
        return new GreetingApplicationRunner();
    }

    /**
     * 这里都配置完成后，必须在spring.factories文件中对该类进行声明，才会真正的起作用。
     */
}
