package geektime.spring.data.mybatisdemo;

import geektime.spring.data.mybatisdemo.mapper.CoffeeMapper;
import geektime.spring.data.mybatisdemo.model.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
/**
 * 使用@MapperScan配置mapper的扫描路径
 */
@MapperScan("geektime.spring.data.mybatisdemo.mapper")
public class MybatisDemoApplication implements ApplicationRunner {

	/**
	 * Mapper 报红只需要在Inspections（点击红色💡->Inspection "Autowiring for Bean Class" options->edit inspection profile setting）
	 * 里面找到spring->spring core->code->Autowiring for Bean Class的Serverity由Error改成Warining就可以了。
	 * 实际上是有这个bean的。
	 */
	@Autowired
	private CoffeeMapper coffeeMapper;

	public static void main(String[] args) {
		SpringApplication.run(MybatisDemoApplication.class, args);
	}

	/**
	 * 可以运行起程序来，看看具体日志
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		Coffee c = Coffee.builder().name("espresso")
				.price(Money.of(CurrencyUnit.of("CNY"), 20.0)).build();
		/**
		 *  @Insert注解 返回的是变动记录的条数，int也是可以的。
		 *  @Update和@Delete返回的是条数
		 */
		Long id = coffeeMapper.save(c);
		log.info("Coffee {} => {}", id, c);

		c = coffeeMapper.findById(id);
		log.info("Coffee {}", c);
	}
}

