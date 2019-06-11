package geektime.spring.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class PropertySourceDemoApplication implements ApplicationRunner {
	/**
	 * 读取geektime.greeting配置属性
	 * 默认情况下可以配置在applicaton.properties文件中
	 * 这里演示如何自定义配置文件
	 */
	@Value("${geektime.greeting}")
	private String greeting;

	public static void main(String[] args) {
		SpringApplication.run(PropertySourceDemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("{}", greeting);
	}
}
