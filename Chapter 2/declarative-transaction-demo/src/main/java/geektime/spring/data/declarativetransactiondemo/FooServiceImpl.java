package geektime.spring.data.declarativetransactiondemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FooServiceImpl implements FooService {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 使用@Transactional注解开启事务
     */
    @Override
    @Transactional
    public void insertRecord() {
        jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('AAA')");
    }

    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public void insertThenRollback() throws RollbackException {
        jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('BBB')");
        throw new RollbackException();
    }


    /**
     * 这个地方要注意，虽然insertThenRollback()这个方法添加了事务，但是
     * invokeInsertThenRollback()方法却没有开启事务，因此整个方法并不会有事务的效果，在invokeInsertThenRollback()里面
     * 调用insertThenRollback()，事务并不起作用。
     * Spring在默认的代理模式下，只有目标方法由外部调用，才能被 Spring 的事务拦截器拦截。在同一个类中的两个方法直接调用，是不会被 Spring 的事务拦截器拦截。
     * 解决的办法有两种：
     * 1、在invokeInsertThenRollback()方法前面加@Transactional注解
     * 2、因为insertThenRollback()方法已经加过了注解，那么可以把自己的实例注入进来，内部方法调用改为直接调用注入的实例。
     *   代码如下:
     *      ...
     *      @Autowired
     *      private FooService fooService;
     *      ...
     *      @Override
     *      public void invokeInsertThenRollback() throws RollbackException{
     *          // 内部方法调用改为直接调用注入的实例
     *          fooService.insertThenRollback();
     *      }
     *      ...
     *
     *
     * @Transactional 事务实现机制
     * 在应用系统调用声明了 @Transactional 的目标方法时，Spring Framework 默认使用 AOP 代理，
     * 在代码运行时生成一个代理对象，根据 @Transactional 的属性配置信息，
     * 这个代理对象决定该声明 @Transactional 的目标方法是否由拦截器 TransactionInterceptor 来使用拦截，
     * 在 TransactionInterceptor 拦截时，会在目标方法开始执行之前创建并加入事务，并执行目标方法的逻辑,
     * 最后根据执行情况是否出现异常，利用抽象事务管理器 AbstractPlatformTransactionManager 操作数据源 DataSource 提交或回滚事务。
     *
     * 事务管理的框架是由抽象事务管理器 AbstractPlatformTransactionManager 来提供的，
     * 而具体的底层事务处理实现，由 PlatformTransactionManager 的具体实现类来实现，
     * 如事务管理器 DataSourceTransactionManager。不同的事务管理器管理不同的数据资源 DataSource，
     * 比如 DataSourceTransactionManager 管理 JDBC 的 Connection。

     *
     */
    @Override
    public void invokeInsertThenRollback() throws RollbackException {
        insertThenRollback();
    }
}
