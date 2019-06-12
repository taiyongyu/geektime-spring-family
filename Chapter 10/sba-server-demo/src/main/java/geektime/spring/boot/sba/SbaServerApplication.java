package geektime.spring.boot.sba;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * spring boot admin server
 * 使用@EnableAdminServer注解
 * 集成WebSecurityConfigurerAdapter类
 *
 * 把server端和client端启动之后  server端会显示 spring boot admin的主界面
 * 会发现有client端注册到server里面，就可以观察client端的各种运行指标
 */
@SpringBootApplication
@EnableAdminServer
public class SbaServerApplication extends WebSecurityConfigurerAdapter {
	@Autowired
	private AdminServerProperties adminServerProperties;

	public static void main(String[] args) {
		SpringApplication.run(SbaServerApplication.class, args);
	}
    // 重写configure接口
	// 具体还需要查一下 spring boot security的用法
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		String adminContextPath = adminServerProperties.getContextPath();

		SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		successHandler.setTargetUrlParameter("redirectTo");
		successHandler.setDefaultTargetUrl(adminContextPath + "/");

		http.authorizeRequests()
				.antMatchers(adminContextPath + "/assets/**").permitAll() // assets和login两个请求不需要进行验证
				.antMatchers(adminContextPath + "/login").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and()
				.logout().logoutUrl(adminContextPath + "/logout").and()
				.httpBasic().and()
				.csrf()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.ignoringAntMatchers(
						adminContextPath + "/instances",
						adminContextPath + "/actuator/**"
				);
	}
}
