package geektime.spring.springbucks.jpademo.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 *
 * 使用了@NoRepositoryBean注解的接口，
 * 不会被单独创建实例，只会作为其他接口的父接口而被使用。
 * BaseRepository 继承了分页和排序的Repository子类
 */
@NoRepositoryBean
public interface BaseRepository<T, Long> extends PagingAndSortingRepository<T, Long> {
    /**
     * 只需要定义接口，不需要手动实现接口
     *
     */
    List<T> findTop3ByOrderByUpdateTimeDescIdAsc();
}
