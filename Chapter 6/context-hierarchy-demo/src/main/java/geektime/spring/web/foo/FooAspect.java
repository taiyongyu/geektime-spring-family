package geektime.spring.web.foo;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
/**
 * 定义一个切面，
 * 使用@AfterReturning注解指明在拦截的以testBean为开头的bean
 * 在要拦截的bean之后，调用printAfter方法进行增强
 */
public class FooAspect {
    @AfterReturning("bean(testBean*)")
    public void printAfter() {
        log.info("after hello()");
    }
}
