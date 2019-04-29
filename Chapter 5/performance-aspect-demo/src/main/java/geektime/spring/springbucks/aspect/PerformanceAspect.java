package geektime.spring.springbucks.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Aspect要配合@Component注解使用
 */
@Aspect
@Component
@Slf4j
public class PerformanceAspect {
    // 下面的配置Around的两种方法都可以。
//    @Around("execution(* geektime.spring.springbucks.repository..*(..))")
    @Around("repositoryOps()")
    public Object logPerformance(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis(); // 执行方法前记时
        String name = "-"; // 要执行的方法的名字
        String result = "Y"; // 执行是否成功
        try {
            name = pjp.getSignature().toShortString(); // 获取方法的名字
            return pjp.proceed(); // 开始执行方法
        } catch (Throwable t) {
            result = "N";
            throw t;
        } finally {
            long endTime = System.currentTimeMillis(); // 执行方法后记时
            log.info("{};{};{}ms", name, result, endTime - startTime);
        }
    }

    /**
     * 拦截repository包路径下的所有类的所有方法
     */
    @Pointcut("execution(* geektime.spring.springbucks.repository..*(..))")
    private void repositoryOps() {
    }
}
