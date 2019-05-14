package geektime.spring.springbucks.waiter.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
@AllArgsConstructor
/**
 * 注意，该自定义的异常类，并没有使用@ControllerAdvice注解
 * 该异常类只能返回状态码 HttpStatus.BAD_REQUEST
 */
public class FormValidationException extends RuntimeException {
    private BindingResult result;
}
