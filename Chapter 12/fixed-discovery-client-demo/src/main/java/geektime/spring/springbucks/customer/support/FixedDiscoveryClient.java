package geektime.spring.springbucks.customer.support;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义实现DiscoverClient
 * 使用@ConfigurationProperties注解加载配置文件中的自定义配置项
 */
@ConfigurationProperties("waiter")
@Setter
public class FixedDiscoveryClient implements DiscoveryClient {
    public static final String SERVICE_ID = "waiter-service";
    // 配置文件中的waiter.services应该是一个列表，在这里配置文件中只配置了一条数据而已
    private List<String> services;

    /**
     * 实现描述
     * @return
     */
    @Override
    public String description() {
        return "DiscoveryClient that uses service.list from application.yml.";
    }

    /**
     * 根据serviceId返回instances
     * @param serviceId
     * @return
     */
    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        if (!SERVICE_ID.equalsIgnoreCase(serviceId)) {
            return Collections.emptyList();
        }
        // 这里忽略了很多边界条件判断，认为就是 HOST:PORT 形式
        return services.stream()
                .map(s -> new DefaultServiceInstance(s,
                        SERVICE_ID,
                        s.split(":")[0],
                        Integer.parseInt(s.split(":")[1]),
                        false)).collect(Collectors.toList());
    }

    /**
     * 返回所有services
     * @return
     */
    @Override
    public List<String> getServices() {
        return Collections.singletonList(SERVICE_ID);
    }
}
