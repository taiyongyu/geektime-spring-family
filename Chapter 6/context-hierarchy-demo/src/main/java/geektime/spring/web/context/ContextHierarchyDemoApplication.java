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
		 * bean = barContext.getBean("testBeanX", TestBean.class);
		 * bean.hello();
		 * 将会获取本context的testBeanX，而不是父context中
		 */
	}
}
