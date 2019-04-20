package geektime.spring.data.mongodemo.converter;

import org.bson.Document;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.core.convert.converter.Converter;

/**
 * 需要将Docuemnt类型和Money类型进行转换
 * 把Price的Doucment传进来
 * 解析出其中的Money、amout、current、code等信息
 * 返回一个Money对象
 */
public class MoneyReadConverter implements Converter<Document, Money> {
    @Override
    public Money convert(Document source) {
        Document money = (Document) source.get("money");
        double amount = Double.parseDouble(money.getString("amount"));
        String currency = ((Document) money.get("currency")).getString("code");
        return Money.of(CurrencyUnit.of(currency), amount);
    }
}


/**
 * 1. 定义的public class MoneyReadConverter implements Converter<Document, Money> 的意思是将Document 转成Money类型
 * 2. 在启动的时候spring会扫描Bean，将MoneyReadConverter 这个Bean注册到GenericConversionService.ConverterAdapter.converter (Converter<Object, Object>类型) 中
 * 3. 在执行query时，从mongo中取出来的“ { "money" : { "currency" : { "code" : "CNY", "numericCode" : 156, "decimalPlaces" : 2 }, "amount" : "20.00" } ”是Document类型的数据，所以触发了自定义的转换器MoneyReadConverter ，会将Docmuent转换成Money类型
 * 4. 要转成什么类型是由这个目标类的成员属性的类型决定的，如果这个位置定义的是个Foo类型，那会找能把Document转成Foo的，如果定义的是个Bar类型，那就会找能把Document转成Bar的。可以多定义几个Converter，分别处理不同的类型。
 */
