package geektime.spring.data.mybatisdemo.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 在 Money 与 Long 之间转换的 TypeHandler，处理 CNY 人民币
 */

/**
 * 无论是 MyBatis 在预处理语句（PreparedStatement）中设置一个参数时，还是从结果集中取出一个值时，
 * 都会用类型处理器将获取的值以合适的方式转换成 Java 类型。
 * Mybatis默认为我们实现了许多TypeHandler, 当我们没有配置指定TypeHandler时，
 * Mybatis会根据参数或者返回结果的不同，默认为我们选择合适的TypeHandler处理。
 * 我们也可以自定义一个TypeHandler，需要继承BaseTypeHandler<T>.T是需要处理的类型
 * 并重写其中的方法。
 */
public class MoneyTypeHandler extends BaseTypeHandler<Money> {
    /**
     * 设置的时候(预处理的时候)，price数据库中是bigint类型，java中是Money类型
     * 要把Money类型转换为long类型。
     * 因此使用 ps.setLong(i, parameter.getAmountMinorLong())进行转换和设置
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Money parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter.getAmountMinorLong());
    }

    /**
     * 从结果中取数的时候，要把long类型转换为Money类型
     */
    @Override
    public Money getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseMoney(rs.getLong(columnName));
    }

    @Override
    public Money getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseMoney(rs.getLong(columnIndex));
    }

    @Override
    public Money getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseMoney(cs.getLong(columnIndex));
    }

    private Money parseMoney(Long value) {
        return Money.of(CurrencyUnit.of("CNY"), value / 100.0);

        // 或者，如下也可以。
        // return Money.ofMinor(CurrencyUnit.of("CNY"), value);
    }
}
