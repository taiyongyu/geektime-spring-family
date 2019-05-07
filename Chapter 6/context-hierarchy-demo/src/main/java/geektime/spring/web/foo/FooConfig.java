package geektime.spring.web.foo;

import geektime.spring.web.context.TestBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 使用@EnableAspectJAutoProxy注解配置对切面的支持
 */
@Configuration
@EnableAspectJAutoProxy
/**
 * 定义配置类，在配置类中定义各种bean
 */
public class FooConfig {
    /**
     * 定义testBeanX bean
     * @return
     */
    @Bean
    public TestBean testBeanX() {
        return new TestBean("foo");
    }

    /**
     * 定义testBeanY bean
     * @return
     */
    @Bean
    public TestBean testBeanY() {
        return new TestBean("foo");
    }

    /**
     * 定义一个fooAspect 切面bean
     * @return
     */
    @Bean
    public FooAspect fooAspect() {
        return new FooAspect();
    }
}
