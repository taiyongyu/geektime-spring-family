package geektime.spring.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
/**
 * 在application中存在多个runner的情况下，
 * 使用@Order注解在标注他们的启动顺序
 */
@Component
@Order(3)
@Slf4j
public class ExitApplicationRunner implements ApplicationRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 获取 exit code
        // 根据SpringApplication.exit()源码可知
        // 如果exit方法中没有明确指出使用何种ExitCodeGenerator
        // 那么就会在context中取出所有ExitCodeGenerator类型的bean
        // 按照取出的exit code先后顺序，后面的覆盖前面的值
        // 最终返回一共exit code。 只要exit code不为0，就说明程序报错。
        int code = SpringApplication.exit(applicationContext);
        log.info("Exit with {}.", code);
        // 利用exit code  退出程序
        System.exit(code);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
