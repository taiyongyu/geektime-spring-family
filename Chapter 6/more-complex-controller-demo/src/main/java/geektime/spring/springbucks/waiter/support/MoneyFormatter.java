package geektime.spring.springbucks.waiter.support;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
/**
 * 使用@Component注解，spring将类自动加载为一个bean。
 * 在持久层、业务层和控制层分别采用 @Repository、@Service 和 @Controller 对分层中的类进行注释，
 * 而用 @Component 对那些比较中立的类进行注释。由于不好说这个类属于哪个层面，就用@Component。
 */
public class MoneyFormatter implements Formatter<Money> {
    /**
     * 处理 CNY 10.00 / 10.00 形式的字符串
     * 校验不太严密，仅作演示。
     *
     * 实现将string转换为Money类型的转换。
     */
    @Override
    public Money parse(String text, Locale locale) throws ParseException {
        if (NumberUtils.isParsable(text)) {
            // 如果仅仅是数字如 10.00
            return Money.of(CurrencyUnit.of("CNY"), NumberUtils.createBigDecimal(text));
        } else if (StringUtils.isNotEmpty(text)) {
            // 不是数字的话，如果形如 CNY 10.00这种形式
            String[] split = StringUtils.split(text, " ");
            if (split != null && split.length == 2 && NumberUtils.isParsable(split[1])) {
                return Money.of(CurrencyUnit.of(split[0]),
                        NumberUtils.createBigDecimal(split[1]));
            } else {
                throw new ParseException(text, 0);
            }
        }
        throw new ParseException(text, 0);
    }

    @Override
    public String print(Money money, Locale locale) {
        if (money == null) {
            return null;
        }
        return money.getCurrencyUnit().getCode() + " " + money.getAmount();
    }
}
