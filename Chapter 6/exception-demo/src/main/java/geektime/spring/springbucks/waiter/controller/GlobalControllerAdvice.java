package geektime.spring.springbucks.waiter.controller;

import geektime.spring.springbucks.waiter.controller.exception.FormValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用@RestControllerAdvice注解，在工程启动的时候自动加载
 * 注解@ControllerAdvice的类可以拥有@ExceptionHandler, @InitBinder或 @ModelAttribute注解的方法，
 * 并且这些方法会被应用到控制器类层次的所有@RequestMapping方法上。
 * 本例中，使用@ExceptionHandler注解来指明这个方法要处理哪种异常。当程序中抛出这种异常时，交由这里的方法来处理
 * 使用@ResponseStatus注解指明这种异常返回何种状态码。
 */

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validationExceptionHandler(ValidationException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("message", exception.getMessage());
        return map;
    }
}
