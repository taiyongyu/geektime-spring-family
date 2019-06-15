package geektime.spring.hello;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class CommandLineApplication {

	public static void main(String[] args) {
		// 两种方式来关闭 web容器
		// 方式一
		new SpringApplicationBuilder(CommandLineApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		// 方式二
		// 根据 application.properties 里的配置来决定 WebApplicationType
//		SpringApplication.run(CommandLineApplication.class, args);
	}
}
