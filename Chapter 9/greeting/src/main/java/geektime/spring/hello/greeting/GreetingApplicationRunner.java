package geektime.spring.hello.greeting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

@Slf4j
/**
 * 注意这里只是一个简单的类，并没有添加@SpringBootApplication、@SpringComponent等等注解。
 * 在创建这个类对象的时候，会调用构造函数。
 */
public class GreetingApplicationRunner implements ApplicationRunner {
    /**
     * 构造函数
     */
    public GreetingApplicationRunner() {

        log.info("Initializing GreetingApplicationRunner default");
    }

    public GreetingApplicationRunner( String name ) {

        log.info("Initializing GreetingApplicationRunner "+name);
    }

    public void run(ApplicationArguments args) throws Exception {
        log.info("Hello everyone! We all like Spring! ");
    }
}
