package geektime.spring.data.datasourcedemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
@Slf4j
/**
 * 使用@Slf4j标签之后可以直接使用log对象进行日志的输出
 * 如果报出找不到log对象错误，那就需要安装lombok插件，并引入lombok依赖
 */
public class DataSourceDemoApplication implements CommandLineRunner {
	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;


	/**
	 *
	 * 每个SpringBoot程序都有一个主入口，也就是main方法，
	 * main里面调用SpringApplication.run()启动整个spring boot程序，
	 * main方法所在类需要使用@SpringBootApplication注解。该注解
	 * @SpringBootApplicatin注解，主要包括三个注解，分别是：
	 * 	@SpringBootConfiguration(内部为@Configuration注解)：标注当前类是配置类，将当前类内声明的一个或多个以@Bean注解标记的方法的实例纳入到spring容器中，并且实例名就是方法名。
	 * 	@EnableAutoConfiguration：SpringBoot根据应用所声明的依赖来对Spring框架进行自动配置
	 * 	@ComponentScan：组件扫描，可自动发现和装配Bean，默认扫描SpringApplication的run方法里的Booter.class所在的包路径下文件，所以最好将该启动类放到根包路径下
	 *
	 */
	public static void main(String[] args) {
		SpringApplication.run(DataSourceDemoApplication.class, args);

		/**
		 * 因为添加了actuator依赖，所以在应用启动后可以通过actuator
		 * 来查看sparing boot默认为配置了哪些beans。
		 * 首先要在application.properties文件中配置：
		 * 	management.endpoints.web.exposure.include=*  来打开所有的端点(endpoint)
		 * 	然后可以通过 http://localhost:8080/actuator/beans 来查看所有配置的beans
		 * 	可以通过 http://localhost:8080/actuator/health 来查看应用的健康状况等
		 *  可以看到，默认的配置的bean有很多，例如 dataSource、jdbcTemplate、transactionTemplate等等
		 *  这些beans通过@Autowired标注加载后就可以直接使用了。
		 */
	}


	/**
	 *重写run方法
	 */
	@Override
	public void run(String... args) throws Exception {
		showConnection();
		showData();
	}

	private void showConnection() throws SQLException {

		log.info("-----------------dataSource:----------------------");
		log.info(dataSource.toString());
		Connection conn = dataSource.getConnection();
		log.info("-----------------conn:----------------------");
		log.info(conn.toString());
		conn.close();

		/**
		 * 由这里的输出可知：
		 * 	我们在应用中使用了H2内存数据库，即使没有在application.properties配置文件中配置数据库的名称和密码，
		 * 	Springboot依然会自动为我们创建一个H2数据库的实例并使用默认的用户名和密码创建链接
		 * 	当然，我们也可以在application.properties文件中进行自定义的配置。
		 */

	}

	private void showData() {
		/**
		 * H2数据库在应用启动的时候加载数据文件，然后就可以在内存中进行查询。
		 */
		jdbcTemplate.queryForList("SELECT * FROM FOO")
				.forEach(row -> log.info(row.toString()));
	}
}

