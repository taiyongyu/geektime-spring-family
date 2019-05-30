package geektime.spring.hello;

import geektime.spring.hello.greeting.GreetingApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 启动这个应用，依赖于geektime-spring-boot-autoconfigure和greeting两个工程，见pom文件。
 */
@SpringBootApplication
public class AutoconfigureDemoApplication {
	/**
	 *
	 * 因为都满足条件，所以按照自动配置的方式，生成GreetingApplicationRunner类型的bean
	 * 会调用GreetingApplicationRunner默认的构造函数
	 * @param args
	 */
	public static void main(String[] args) {

		SpringApplication.run(AutoconfigureDemoApplication.class, args);
	}

	/**
	 * 手动生成GreetingApplicationRunner类型的bean，因为不满足条件，所以不会按照自动配置方式生成GreetingApplicationRunner类型的bean
	 * 会调用GreetingApplicationRunner带参数的构造函数
	 */
	@Bean
	public GreetingApplicationRunner greetingApplicationRunner(){
		return new GreetingApplicationRunner("Spring boot ");

	}


}
