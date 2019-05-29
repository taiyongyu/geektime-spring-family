package geektime.spring.web.session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@SpringBootApplication
@RestController
@EnableRedisHttpSession
/**
 * 使用@EnableRedisHttpSession 注解
 */
public class SessionDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SessionDemoApplication.class, args);
	}

	/**
	 * 从request中取出session中的name属性。session已经是集中会话的方式
	 * @param session
	 * @param name
	 * @return
	 */
	@RequestMapping("/hello")
	public String printSession(HttpSession session, String name) {
		// 取
		String storedName = (String) session.getAttribute("name");
		if (storedName == null) {
			// 存
			session.setAttribute("name", name);
			storedName = name;
		}
		return "hello " + storedName;
	}
	/**
	 * 只要session id不发生变化，每次取出的session都是一样的。
	 * 举个例子，采用集中式访问web应用后，会在redis中存储下session。
	 * 这时候如果后台服务宕机，重启，单机模式下session肯定丢失了。
	 * 而集中式下，只要不进行刷新页面，仍然具有相同的session id，
	 * 如果再次访问该地址仍然会得到与之前相同的session。
	 */

}
