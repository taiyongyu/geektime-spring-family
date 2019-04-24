package geektime.spring.springbucks.converter;

import org.joda.money.Money;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.nio.charset.StandardCharsets;

/**
 * @WritingConverter注解，标注在写入redis的时候进行转换
 */
@WritingConverter
public class MoneyToBytesConverter implements Converter<Money, byte[]> {
    @Override
    public byte[] convert(Money source) {
        String value = Long.toString(source.getAmountMinorLong());
        return value.getBytes(StandardCharsets.UTF_8);
    }
}
