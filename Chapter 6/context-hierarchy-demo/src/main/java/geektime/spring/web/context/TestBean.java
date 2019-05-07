package geektime.spring.web.context;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
/**
 * 使用aop要进行增强的bean
 */
public class TestBean {
    private String context;

    public void hello() {
        log.info("hello " + context);
    }


}
