package geektime.spring.springbucks.waiter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 实现HandlerInterceptor接口并复写其中的方法。
 */

@Slf4j
public class PerformanceInteceptor implements HandlerInterceptor {
    /**
     * StopWatch是用来监控请求执行时间、性能监控的工具类。
     */
    private ThreadLocal<StopWatch> stopWatch = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        StopWatch sw = new StopWatch();
        stopWatch.set(sw);
        // 在方法执行之前，开启监控
        sw.start();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 方法执行之后，关闭监控
        stopWatch.get().stop();
        // 再次开启监控，监控呈现时间
        stopWatch.get().start();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        StopWatch sw = stopWatch.get();
        // 呈现结束后，关闭监控
        sw.stop();
        String method = handler.getClass().getSimpleName();
        if (handler instanceof HandlerMethod) {
            String beanType = ((HandlerMethod) handler).getBeanType().getName();
            String methodName = ((HandlerMethod) handler).getMethod().getName();
            method = beanType + "." + methodName;
        }

        /**
         * 把所有信息打印出来，包括:
         *  请求的url;
         *  具体执行的方法;
         *  方法执行结果状态码
         *  是否有异常及异常类型
         *  总耗时
         *  方法执行耗时
         *  结果呈现耗时
         *
         */

        log.info("{};{};{};{};{}ms;{}ms;{}ms", request.getRequestURI(), method,
                response.getStatus(), ex == null ? "-" : ex.getClass().getSimpleName(),
                sw.getTotalTimeMillis(), sw.getTotalTimeMillis() - sw.getLastTaskTimeMillis(),
                sw.getLastTaskTimeMillis());
        stopWatch.remove();
        /**
         * 可以把工程启动起来，发送请求看看效果。如：
         * 发送请求 curl http://localhost:8080/coffee/1
         * 返回json:
         *  {
         *   "id": 2,
         *   "createTime": "2019-05-14T15:05:47.480+0800",
         *   "updateTime": "2019-05-14T15:05:47.480+0800",
         *   "name": "latte",
         *   "price": 25.00
         * }
         * 都是经过格式化缩进并采用上海时间的格式
         * 后台控制台输出：
         *  /coffee/2;geektime.spring.springbucks.waiter.controller.CoffeeController.getById;200;-;6ms;5ms;1ms
         *
         *
         *
         */
    }
}
