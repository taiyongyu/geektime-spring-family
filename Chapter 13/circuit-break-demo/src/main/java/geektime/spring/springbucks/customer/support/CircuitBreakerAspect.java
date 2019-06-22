package geektime.spring.springbucks.customer.support;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 不使用任何的框架，用AOP来实现断路保护
 */
@Aspect
@Component
@Slf4j
public class CircuitBreakerAspect {
    private static final Integer THRESHOLD = 3;
    // counter记录失败调用的情况
    private Map<String, AtomicInteger> counter = new ConcurrentHashMap<>();
    // breakCounter记录断路保护的次数
    private Map<String, AtomicInteger> breakCounter = new ConcurrentHashMap<>();

    // 拦截integration路径下，所有方法的调用。
    @Around("execution(* geektime.spring.springbucks.customer.integration..*(..))")
    public Object doWithCircuitBreaker(ProceedingJoinPoint pjp) throws Throwable {
        // 通过ProceedingJoinPoint获得当前正在执行的方法，即切入点(连接点)。
        // 获取连接点的方法签名对象。后面用作key
        String signature = pjp.getSignature().toLongString();
        log.info("Invoke {}", signature);
        Object retVal;
        try {
            if (counter.containsKey(signature)) {
                // 包含该signature，判断失败次数是否已到阈值并且断路保护次数还没到阈值
                // 如果为true，则进入断路保护
                if (counter.get(signature).get() > THRESHOLD &&
                        breakCounter.get(signature).get() < THRESHOLD) {
                    log.warn("Circuit breaker return null, break {} times.",
                            breakCounter.get(signature).incrementAndGet());
                    return null;
                }
            } else {
                // 不包含该signature，做一个初始化
                counter.put(signature, new AtomicInteger(0));
                breakCounter.put(signature, new AtomicInteger(0));
            }
            // 实际调用该方法
            retVal = pjp.proceed();
            // 方法调用成功，则所有计数器清零
            counter.get(signature).set(0);
            breakCounter.get(signature).set(0);
        } catch (Throwable t) {
            // 方法调用不成功，则失败次数+1，熔断次数清零。
            // 达到的效果就是程序在调用失败达到阈值次数之后，开始进行断路保护。
            // 断路保护达到阈值次数之后，再来试探该方法调用是否能够成功。
            log.warn("Circuit breaker counter: {}, Throwable {}",
                    counter.get(signature).incrementAndGet(), t.getMessage());
            breakCounter.get(signature).set(0);
            throw t;
        }
        return retVal;
    }
}
