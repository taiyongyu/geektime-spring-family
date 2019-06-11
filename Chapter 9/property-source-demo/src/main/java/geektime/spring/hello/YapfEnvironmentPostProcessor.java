package geektime.spring.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
/**
 * 使用EnvironmentPostProcessor加载外部配置资源
 * 1.实现EnvironmentPostProcessor接口,重写postProcessEnvironment方法
 * 2.在META-INF下创建spring.factories文件，为EnvironmentPostProcessor接口指定类全路径名称
 *
 */
@Slf4j
public class YapfEnvironmentPostProcessor implements EnvironmentPostProcessor {
    /**
     * SpringBoot 的配置文件内置支持 properties、xml、yml、yaml 几种格式。
     * 其中 properties和xml对应的Loader类为 PropertiesPropertySourceLoader 。
     * yml和yaml对应的Loader类为 YamlPropertySourceLoader。
     * 观察这2个类可以发现，都实现自接口 PropertySourceLoader 。
     */
    private PropertiesPropertySourceLoader loader = new PropertiesPropertySourceLoader();
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // 根据上下文环境获取配置属性
        // MutablePropertySources类，它包含了一个CopyOnWriteArrayList集合，用来包含多个PropertySource。
        MutablePropertySources propertySources = environment.getPropertySources();
        // 外部配置文件(即自定义配置文件)在resources文件夹下面的yapf.properties文件
        Resource resource = new ClassPathResource("yapf.properties");
        try {
            PropertySource ps = loader.load("YetAnotherPropertiesFile", resource) // 返回一个List
                    .get(0);
            // 添加自定义配置
            propertySources.addFirst(ps);
        } catch (Exception e) {
            log.error("Exception!", e);
        }
    }
}
