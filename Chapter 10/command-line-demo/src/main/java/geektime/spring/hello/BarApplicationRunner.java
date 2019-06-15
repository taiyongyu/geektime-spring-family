package geektime.spring.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
/**
 * 在application中存在多个runner的情况下，
 * 使用@Order注解在标注他们的启动顺序
 */
@Component
@Order(2)
@Slf4j
public class BarApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Bar");
    }
}
