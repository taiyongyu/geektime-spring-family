package geektime.spring.cloud.turbine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * 使用@EnableTurbine接口开启即可
 */
@SpringBootApplication
@EnableTurbine
public class TurbineDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TurbineDemoApplication.class, args);
	}

}
