package geektime.spring.web.context;

import geektime.spring.web.foo.FooConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
@Slf4j
public class ContextHierarchyDemoApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(ContextHierarchyDemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 使用AnnotationConfigApplicationContext实现基于Java的配置类加载Spring的应用上下文。
		// 避免使用application.xml进行配置。相比XML配置，更加便捷。
		ApplicationContext fooContext = new AnnotationConfigApplicationContext(FooConfig.class);
		// 使用ClassPathXmlApplicationContext实现基于读取xml配置文件的方式加载Spring的应用上下文，
		// 可以在String[]数组中指定多个配置文件，
		// 第二个参数为指定其父ApplicationContext，实现继承关系
		// 在applicationContext.xml中定义了一个testBeanX，但是它的context为bar
		ClassPathXmlApplicationContext barContext = new ClassPathXmlApplicationContext(
				new String[] {"applicationContext.xml"}, fooContext);
		TestBean bean = fooContext.getBean("testBeanX", TestBean.class);
		bean.hello();

		log.info("=============");
        // 注意区分这里的testBeanX是从哪里继承的，是父context中还是自己本身的context中
		bean = barContext.getBean("testBeanX", TestBean.class);
		bean.hello();

        // 调用父context中的方法
		bean = barContext.getBean("testBeanY", TestBean.class);
		bean.hello();

		/**
		 * 程序执行说明：
		 * 1、
		 * bean = barContext.getBean("testBeanX", TestBean.class);
		 * bean.hello();
		 * 由于在xml文件中做了覆盖，将会获取本context的testBeanX，而不是父context中的testBeanX
		 * 因此调用bean.hello()不会被切面做增强处理，即不会输出after hello()。
		 * 而bean = barContext.getBean("testBeanY", TestBean.class);
		 * bean.hello();
		 * 没有进行覆盖，仍然将获取父context中的bean
		 * 因此调用bean.hello()将会被切面做增强处理，输出after hello()。
		 *
		 * 2、
		 * 如果注释掉FooConfig.java中的
		 * @Bean
		 *     public FooAspect fooAspect() {
		 *         return new FooAspect();
		 *     }
		 * 而在xml配置文件中新增bean:
		 * <aop:aspectj-autoproxy/>
		 * 以及：
		 * <bean id="fooAspect" class="geektime.spring.web.foo.FooAspect" />
		 * 那么，aop的增强，就被加在了本context中，父context中将不再增强。
		 *
		 * 这就说明，如果只想对底层的bean做增强，那么aop就要加在底层的bean上面
		 * 如果只想对上层的bean做增强，那么就把aop放到上层的aop中。
		 *
		 * 3、如果想做通用的增强，即底层和上层的aop，都想做增强，应该如何配置？
		 * 在FooConfig.java中放开
		 *     @Bean
		 *     public FooAspect fooAspect() {
		 *         return new FooAspect();
		 *     }
		 * 同时在xml文件中增加：
		 * <aop:aspectj-autoproxy/>
		 * 而注释掉
		 * <bean id="fooAspect" class="geektime.spring.web.foo.FooAspect" />
		 * 这样，在所有的底层和上层的bean，都将做增强。
		 *
		 * 4、
		 * <aop:aspectj-autoproxy />的作用：
		 * 通过aop命名空间的<aop:aspectj-autoproxy />声明自动为spring容器中那些配置@aspectJ切面的bean创建代理，织入切面。
		 *
		 */
	}
}
